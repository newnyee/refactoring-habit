package com.refactoringhabit.stats.domain.batch.step;

import com.refactoringhabit.order.domain.repository.OrderRepository;
import com.refactoringhabit.order.dto.OrderSummaryDto;
import com.refactoringhabit.review.domain.repository.ReviewRepository;
import com.refactoringhabit.review.dto.ReviewSummaryDto;
import com.refactoringhabit.stats.domain.batch.listener.CustomChunkListener;
import com.refactoringhabit.stats.domain.batch.listener.CustomStepListener;
import com.refactoringhabit.stats.domain.entity.HostDailySalesStats;
import com.refactoringhabit.stats.domain.mapper.StatsEntityMapper;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
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
public class HostDailySalesStatsUpdateStepConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public static final String HOST_DAILY_SALES_STATS_ALT_ID = null;

    @Bean(name = "hostDailySalesStatsUpdateStep")
    public Step hostDailySalesStatsUpdateStep(JobRepository jobRepository,
        CustomStepListener stepListener, PlatformTransactionManager transactionManager,
        CustomChunkListener chunkListener) {
        return new StepBuilder("hostDailySalesStatsUpdateStep", jobRepository)
            .listener(stepListener)
            .<Long, HostDailySalesStats>chunk(500, transactionManager)
            .reader(hostDailySalesStatsReader())
            .processor(hostDailySalesStatsUpdateProcessor())
            .writer(hostDailySalesStatsUpdateWriter())
            .faultTolerant()
            .retryLimit(3)
            .retry(DataAccessException.class)
            .skipLimit(5)
            .skip(DataAccessException.class)
            .listener(chunkListener)
            .build();
    }

    private JpaPagingItemReader<Long> hostDailySalesStatsReader() {
        return new JpaPagingItemReaderBuilder<Long>()
            .name("hostDailySalesStatsReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(500)
            .queryString("select p.id from Host p")
            .build();
    }

    private ItemProcessor<Long, HostDailySalesStats> hostDailySalesStatsUpdateProcessor() {
        return hostId -> {
            LocalDate yesterday = LocalDate.now().minusDays(1);

            OrderSummaryDto orderSummaryDto =
                orderRepository.orderSummaryByHostIdAndDate(hostId, yesterday); // 어제의 판매량, 판매 금액

            Long refundCount = orderRepository.orderRefundCountByHostIdAndDate(hostId, yesterday); // 어제의 환불 건수

            ReviewSummaryDto reviewSummaryDto =
                reviewRepository.reviewSummaryByHostIdAndDate(hostId, yesterday); // 어제의 리뷰 수, 리뷰 평점

            return StatsEntityMapper.INSTANCE.toHostDailySalesStatsEntity(
                hostId, orderSummaryDto, refundCount,
                reviewSummaryDto, HOST_DAILY_SALES_STATS_ALT_ID);
        };
    }

    private JpaItemWriter<HostDailySalesStats> hostDailySalesStatsUpdateWriter() {
        JpaItemWriter<HostDailySalesStats> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
