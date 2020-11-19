package com.pay.money.throwing.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RandomDistributorTest {

    @DisplayName("분배 로직 테스트")
    @ParameterizedTest
    @MethodSource
    void distribute(BigDecimal money, int personCount) {
        RandomMoneyDistributor randomDistributor = new RandomMoneyDistributor();
        List<BigDecimal> distribute = randomDistributor.distribute(money, personCount);
        System.out.println(Arrays.toString(distribute.toArray()));
        assertThat(distribute.stream().reduce(BigDecimal::add).get()).isEqualTo(money);
    }

    static Stream<Arguments> distribute() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(10000), 3),
                Arguments.of(BigDecimal.valueOf(10000), 4),
                Arguments.of(BigDecimal.valueOf(100000), 2),
                Arguments.of(BigDecimal.valueOf(1), 1),
                Arguments.of(BigDecimal.valueOf(3), 2)
        );
    }
}