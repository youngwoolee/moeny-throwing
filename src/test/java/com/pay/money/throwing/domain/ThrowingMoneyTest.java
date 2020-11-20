package com.pay.money.throwing.domain;

import com.pay.money.throwing.repository.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ThrowingMoneyTest {
    private ThrowingMoney throwingMoney;
    private List<ReceivingMoney> receivingMoneyList;

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

        receivingMoneyList = Arrays.asList(
            ReceivingMoney.builder()
                    .money(BigDecimal.valueOf(1000))
                    .userId(2L)
                    .build(),
            ReceivingMoney.builder()
                    .money(BigDecimal.valueOf(2000))
                    .userId(3L)
                    .build()
            );

        for(ReceivingMoney receivingMoney : receivingMoneyList) {
            throwingMoney.addReceivingMoney(receivingMoney);
        }
    }

    @DisplayName("동일 유저인지 판단")
    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false", "3:false"}, delimiter = ':')
    public void isSameUser(Long userId, boolean expected) {
        assertThat(throwingMoney.isSameUser(userId)).isEqualTo(expected);
    }

    @DisplayName("받은 금액 총금액 계산")
    @Test
    public void getTotalReceivingMoney() {
        assertThat(throwingMoney.getTotalReceivingMoney()).isEqualTo(BigDecimal.valueOf(3000));
    }

    @DisplayName("이미 받은 유저 체크")
    @ParameterizedTest
    @CsvSource(value = {"1:false", "2:true", "3:true"}, delimiter = ':')
    public void isAlreadyReceivedUser(Long userId, boolean expected) {
        assertThat(throwingMoney.isReceived(userId)).isEqualTo(expected);
    }

}