package com.pay.money.throwing.endpoint.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ThrowingMoneyRequestTest {

    @DisplayName("뿌리기용 dto 생성 성공")
    @ParameterizedTest
    @MethodSource
    public void createThrowingMoneyRequest(BigDecimal money, int personCount) {
        ThrowingMoneyRequest throwingMoneyRequest = new ThrowingMoneyRequest(money, personCount);
        assertDoesNotThrow(throwingMoneyRequest::validationThenException);
    }

    static Stream<Arguments> createThrowingMoneyRequest() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(1), 5),
                Arguments.of(BigDecimal.valueOf(10000), 5)
        );
    }


    @DisplayName("뿌리기용 dto 생성 실패 : 뿌리는 돈이 0 보다 크고 받을 인원이 0 보다 커야함")
    @ParameterizedTest
    @MethodSource
    public void createThrowingMoneyRequestException(BigDecimal money, int personCount) {
        ThrowingMoneyRequest throwingMoneyRequest = new ThrowingMoneyRequest(money, personCount);
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(throwingMoneyRequest::validationThenException);
    }

    static Stream<Arguments> createThrowingMoneyRequestException() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(0), 5),
                Arguments.of(BigDecimal.valueOf(-1), 5),
                Arguments.of(BigDecimal.valueOf(10000), 0),
                Arguments.of(BigDecimal.valueOf(20000), -1)
        );
    }

}