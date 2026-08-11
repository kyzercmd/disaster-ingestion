package com.bd.disaster.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    @Value("${disaster.thread.pool.size:4}")
    private int poolSize;

    @Bean(destroyMethod = "shutdown")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(poolSize);
    }
}
