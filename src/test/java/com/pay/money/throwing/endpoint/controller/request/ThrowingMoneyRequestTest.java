package com.pay.money.throwing.endpoint.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ThrowingMoneyRequestTest {

    @DisplayName("뿌리기용 dto 생성 성공")
    @ParameterizedTest
    @MethodSource
    public void createThrowingMoneyRequest(BigDecimal money, int personCount) {
        assertDoesNotThrow(() -> new ThrowingMoneyRequest(money, personCount));
    }

    static Stream<Arguments> createThrowingMoneyRequest() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(1), 5),
                Arguments.of(BigDecimal.valueOf(10000), 5)
        );
    }


}