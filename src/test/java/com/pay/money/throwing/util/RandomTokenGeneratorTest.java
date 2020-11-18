package com.pay.money.throwing.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RandomTokenGeneratorTest {

    @DisplayName("토큰 생성 : 문자열 3자리 랜덤 생성")
    @Test
    void generate() {
        int length = 3;
        RandomTokenGenerator randomTokenGenerator = RandomTokenGenerator.of(length);
        String token = randomTokenGenerator.generate();
        System.out.println(token);
        assertThat(token.length()).isEqualTo(3);
    }
}