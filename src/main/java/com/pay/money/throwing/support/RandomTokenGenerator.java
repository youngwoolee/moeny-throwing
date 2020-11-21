package com.pay.money.throwing.support;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomTokenGenerator implements TokenGeneratorStrategy{

    private final int length;

    private RandomTokenGenerator(final int length) {
        this.length = length;
    }

    public static RandomTokenGenerator of(final int length) {
        return new RandomTokenGenerator(length);
    }

    @Override
    public String generate() {
        return RandomStringUtils.randomAlphabetic(length);
    }
}
