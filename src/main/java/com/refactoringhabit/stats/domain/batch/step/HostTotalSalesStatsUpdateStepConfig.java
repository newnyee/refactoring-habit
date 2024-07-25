package com.refactoringhabit.stats.domain.batch.step;

import com.refactoringhabit.order.domain.repository.OrderRepository;
import com.refactoringhabit.order.dto.OrderSummaryDto;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.batch.listener.CustomChunkListener;
import com.refactoringhabit.stats.domain.batch.listener.CustomStepListener;
import com.refactoringhabit.stats.domain.entity.HostTotalSalesStats;
import com.refactoringhabit.stats.domain.mapper.StatsEntityMapper;
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
public class HostTotalSalesStatsUpdateStepConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Bean(name = "hostTotalSalesStatsUpdateStep")
    public Step hostTotalSalesStatsUpdateStep(JobRepository jobRepository,
        CustomStepListener stepListener, PlatformTransactionManager transactionManager,
        CustomChunkListener chunkListener) {
        return new StepBuilder("hostTotalSalesStatsUpdateStep", jobRepository)
            .listener(stepListener)
            .<HostTotalSalesStats, HostTotalSalesStats>chunk(500, transactionManager)
            .reader(hostTotalSalesStatsReader())
            .processor(hostTotalSalesStatsUpdateProcessor())
            .writer(hostTotalSalesStatsUpdateWriter())
            .faultTolerant()
            .retryLimit(3)
            .retry(DataAccessException.class)
            .skipLimit(5)
            .skip(DataAccessException.class)
            .listener(chunkListener)
            .build();
    }

    private JpaPagingItemReader<HostTotalSalesStats> hostTotalSalesStatsReader() {
        return new JpaPagingItemReaderBuilder<HostTotalSalesStats>()
            .name("HostTotalSalesStatsReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(500)
            .queryString("select p from HostTotalSalesStats p")
            .build();
    }

    private ItemProcessor<HostTotalSalesStats, HostTotalSalesStats> hostTotalSalesStatsUpdateProcessor() {
        return hostTotalSalesStats -> {
            Long hostId = hostTotalSalesStats.getHostId();

            OrderSummaryDto orderSummaryDto =
                orderRepository.orderSummaryByHostId(hostId); // 판매량, 판매 금액

            Long refundCount = orderRepository.orderRefundCountByHostId(hostId); // 환불 건수

            ReviewSummaryDto reviewSummaryDto = reviewRepository.reviewSummaryByHostId(hostId); // 리뷰 수, 리뷰 평점

            StatsEntityMapper.INSTANCE.updateHostTotalSalesStatsEntity(
                hostTotalSalesStats, orderSummaryDto, refundCount, reviewSummaryDto);

            return hostTotalSalesStats;
        };
    }

    private JpaItemWriter<HostTotalSalesStats> hostTotalSalesStatsUpdateWriter() {
        JpaItemWriter<HostTotalSalesStats> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
