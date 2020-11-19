package com.pay.money.throwing.config;

import com.pay.money.throwing.util.RandomMoneyDistributor;
import com.pay.money.throwing.util.RandomTokenGenerator;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    private static final int LIMIT_SIZE = 3;

    @Bean
    public TokenGeneratorStrategy tokenGenerator() {
        return RandomTokenGenerator.of(LIMIT_SIZE);
    }

    @Bean
    public RandomMoneyDistributor randomDistributor() {
        return new RandomMoneyDistributor();
    }
}
