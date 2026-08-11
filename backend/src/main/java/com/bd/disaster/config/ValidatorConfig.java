package com.bd.disaster.config;

import com.bd.disaster.validation.EventValidator;
import com.bd.disaster.validation.StrictEventValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {
    @Bean
    public EventValidator eventValidator() {
        return new StrictEventValidator();
    }
}
