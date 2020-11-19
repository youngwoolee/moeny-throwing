package com.pay.money.throwing.endpoint.controller.response;

import com.pay.money.throwing.domain.ReceivingMoney;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThrowingMoneyResponse {
    private LocalDateTime createdAt;
    private BigDecimal throwingMoney;
    private BigDecimal receivingMoney;
    private List<ReceivingMoneyResponse> receivingMoneyList;

    public static ThrowingMoneyResponse valueOf(LocalDateTime createdAt, BigDecimal throwingMoney, List<ReceivingMoney> receivingMoneyList) {
        BigDecimal receivingMoney = receivingMoneyList.stream().map(ReceivingMoney::getMoney).reduce(BigDecimal::add).get();
        return ThrowingMoneyResponse.builder()
                    .createdAt(createdAt)
                    .throwingMoney(throwingMoney)
                    .receivingMoney(receivingMoney)
                    .receivingMoneyList(ReceivingMoneyResponse.from(receivingMoneyList))
                .build();
    }


}
