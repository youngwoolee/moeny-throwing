package com.pay.money.throwing.endpoint.controller.request;

import com.pay.money.throwing.domain.ThrowingMoney;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThrowingMoneyRequest {

    @NotNull
    @Min(value = 1, message = "뿌리는 돈이 0 이상 이어야한다")
    private BigDecimal money;
    @NotNull
    @Min(value = 1, message = "받을 인원이 0 이상 이어야한다")
    private Integer personCount;

    public ThrowingMoney toEntity(final Long userId, final String roomId, final String token) {
        return ThrowingMoney.builder()
                .userId(userId)
                .roomId(roomId)
                .token(token)
                .money(this.money)
                .personCount(this.personCount)
                .build();
    }
}
