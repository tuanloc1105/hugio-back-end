package vn.com.hugio.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

    @Bean("hugioThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor hugioThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(30);
        taskExecutor.setMaxPoolSize(40);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("hugioThreadPoolTaskExecutor");
        taskExecutor.initialize();
        //taskExecutor.execute(() -> System.out.println("ok"));
        return taskExecutor;
    }

}
