package com.pay.money.throwing.config;

import com.pay.money.throwing.interceptor.HeaderInterceptor;
import com.pay.money.throwing.support.RandomMoneyDistributor;
import com.pay.money.throwing.support.RandomTokenGenerator;
import com.pay.money.throwing.support.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final int LIMIT_SIZE = 3;
    private final HeaderInterceptor headerInterceptor;

    @Bean
    public TokenGeneratorStrategy tokenGenerator() {
        return RandomTokenGenerator.of(LIMIT_SIZE);
    }

    @Bean
    public RandomMoneyDistributor randomDistributor() {
        return new RandomMoneyDistributor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/api/v1/money/throwing/**");
    }
}
