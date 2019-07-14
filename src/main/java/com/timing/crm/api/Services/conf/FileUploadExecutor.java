package com.timing.crm.api.Services.conf;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class FileUploadExecutor {

    @Value("${fileupload.thread.core-pool}")
    private int corePoolSize;

    @Value("${fileupload.thread.max-pool}")
    private int maxPoolSize;

    @Value("${fileupload.queue.capacity}")
    private int queueCapacity;

    @Bean
    @Qualifier("fileUploadExecutor")
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("FileUpload-Exec-");
        executor.initialize();
        return executor;
    }

}
