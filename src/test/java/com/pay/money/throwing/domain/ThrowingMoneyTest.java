package com.pay.money.throwing.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ThrowingMoneyTest {
    ThrowingMoney throwingMoney;

    @BeforeEach
    public void setUp() {
        throwingMoney =
                ThrowingMoney.builder()
                .userId(1L)
                .roomId("room")
                .token("abc")
                .money(BigDecimal.valueOf(10000))
                .personCount(3)
                .build();
    }

    @DisplayName("동일 유저인지 판단")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false", "3:false"}, delimiter = ':')
    public void isSameUser(Long userId, boolean expected) {
        assertThat(throwingMoney.isSameUser(userId)).isEqualTo(expected);
    }

}