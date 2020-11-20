package com.pay.money.throwing.endpoint.controller.request;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThrowingMoneyRequest {

    @NotNull
    private BigDecimal money;
    @NotNull
    private Integer personCount;

    public void validationThenException() {
        if(isMoneyLessThanZero()) {
            throw new ApiSystemException(ErrorCode.IS_MONEY_LESS_THAN_ZERO);
        }
        if(this.personCount <= 0) {
            throw new ApiSystemException(ErrorCode.IS_PERSON_COUNT_GREATER_THAN_ZERO);
        }
    }

    private boolean isMoneyLessThanZero() {
        return (BigDecimal.ZERO).compareTo(money) >= 0;
    }

    public ThrowingMoney toEntity(Long userId, String roomId, String token) {
        return ThrowingMoney.builder()
                .userId(userId)
                .roomId(roomId)
                .token(token)
                .money(this.money)
                .personCount(this.personCount)
                .build();
    }
}
