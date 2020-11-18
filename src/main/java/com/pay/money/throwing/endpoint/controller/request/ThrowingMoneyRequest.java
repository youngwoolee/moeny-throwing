package com.pay.money.throwing.endpoint.controller.request;

import com.pay.money.throwing.domain.ThrowingMoney;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThrowingMoneyRequest {
    private BigDecimal money;
    private int personCount;

    public void validationThenException() {
        if(isMoneyLessThanZero()) {
            throw new RuntimeException("뿌리는 돈이 0 이상 이어야한다");
        }
        if(this.personCount <= 0) {
            throw new RuntimeException("받을 인원이 0 이상 이어야한다");
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
