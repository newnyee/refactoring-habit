package com.refactoringhabit.stats.domain.batch.step;

import com.refactoringhabit.order.domain.repository.OrderRepository;
import com.refactoringhabit.order.dto.OrderSummaryDto;
import com.refactoringhabit.product.domain.repository.ProductRepository;
import com.refactoringhabit.product.domain.repository.RedisRepository;
import com.refactoringhabit.product.dto.ProductSummaryDto;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.batch.listener.CustomChunkListener;
import com.refactoringhabit.stats.domain.batch.listener.CustomStepListener;
import com.refactoringhabit.stats.domain.entity.ProductTotalSalesStats;
import com.refactoringhabit.stats.domain.mapper.StatsEntityMapper;
import com.refactoringhabit.wish.domain.repository.WishRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProductTotalSalesStatsUpdateStepConfig {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final ReviewRepository reviewRepository;
    private final RedisRepository redisRepository;
    private final EntityManagerFactory entityManagerFactory;

    private static final String VIEW_COUNT_CACHE_PREFIX = "view-count::";

    @Bean(name = "productTotalSalesStatsUpdateStep")
    public Step productTotalSalesStatsUpdateStep(JobRepository jobRepository,
        CustomStepListener stepListener, PlatformTransactionManager transactionManager,
        CustomChunkListener chunkListener) {
        return new StepBuilder("productTotalSalesStatsUpdateStep", jobRepository)
            .listener(stepListener)
            .<ProductTotalSalesStats, ProductTotalSalesStats>chunk(500, transactionManager) // 커밋 간격
            .reader(productTotalSalesStatsReader())
            .processor(productTotalSalesStatsUpdateProcessor())
            .writer(productTotalSalesStatsUpdateWriter())
            .faultTolerant() // 배치 작업 실패 시 처리 방법 설정
            .retryLimit(3)
            .retry(DataAccessException.class)
            .skipLimit(5)
            .skip(DataAccessException.class)
            .listener(chunkListener)
            .build();
    }

    private JpaPagingItemReader<ProductTotalSalesStats> productTotalSalesStatsReader() {
        return new JpaPagingItemReaderBuilder<ProductTotalSalesStats>()
            .name("productTotalSalesStatsUpdateReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(500) // 해당 데이터를 메모리에 몇개씩 올려서 작업할 지 설정
            .queryString("select p from ProductTotalSalesStats p")
            .build();
    }

    private ItemProcessor<ProductTotalSalesStats, ProductTotalSalesStats> productTotalSalesStatsUpdateProcessor() {
        return productTotalSalesStats -> {
            Long productId = productTotalSalesStats.getProductId();

            OrderSummaryDto orderSummaryDto =
                orderRepository.orderSummaryByProductId(productId); // 판매량, 판매금액

            ProductSummaryDto productSummaryDto =
                productRepository.findMinAndMaxProductPrice(productId);// 최소 가격, 최대 가격

            ReviewSummaryDto reviewSummaryDto =
                reviewRepository.reviewSummaryByProductId(productId); // 리뷰 수, 리뷰 평점

            Long viewCount = getViewCount(productTotalSalesStats.getAltId(),
                productTotalSalesStats.getViewCount());// 조회 수

            Long wishCount = wishRepository.countByProductId(productId); // 찜 수

            StatsEntityMapper.INSTANCE.updateProductTotalSalesStatsEntity(
                productTotalSalesStats, orderSummaryDto, productSummaryDto, reviewSummaryDto, viewCount, wishCount);
            return productTotalSalesStats;
        };
    }

    private JpaItemWriter<ProductTotalSalesStats> productTotalSalesStatsUpdateWriter() {
        JpaItemWriter<ProductTotalSalesStats> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    private Long getViewCount(String productAltId, Long oldViewCount) {
        Long todayViewCount = redisRepository
            .getLongValue(VIEW_COUNT_CACHE_PREFIX + productAltId);
        if (todayViewCount > 0) {
            redisRepository.setLongValue(VIEW_COUNT_CACHE_PREFIX + productAltId, 0L);
        }
        return oldViewCount + todayViewCount;
    }
}
