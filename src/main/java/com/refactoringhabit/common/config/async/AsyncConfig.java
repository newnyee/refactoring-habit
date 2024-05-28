package com.refactoringhabit.common.config.async;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "emailAsyncExecutor")
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(12); // 서버의 물리적인 코어 수 * 2 (I/O-bound 작업)
        executor.setMaxPoolSize(24);
        executor.setQueueCapacity(100);
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix("Asynchronous Mail Sender Thread-");
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
            log.error(
                "Asynchronous method thrown exception... -> Method = {}, Params = {}",
                method, params, ex
            );
    }
}
