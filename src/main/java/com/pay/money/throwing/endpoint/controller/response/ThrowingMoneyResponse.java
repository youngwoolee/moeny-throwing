package com.pay.money.throwing.endpoint.controller.response;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThrowingMoneyResponse {
    private LocalDateTime throwingDate;
    private BigDecimal throwingMoney;
    private BigDecimal receivingMoney;
    private List<ReceivingMoneyResponse> receivingMoneyList;

    public static ThrowingMoneyResponse of(ThrowingMoney throwingMoney) {
        return ThrowingMoneyResponse.builder()
                    .throwingDate(throwingMoney.getCreatedAt())
                    .throwingMoney(throwingMoney.getMoney())
                    .receivingMoney(throwingMoney.getTotalReceivingMoney())
                    .receivingMoneyList(ReceivingMoneyResponse.from(throwingMoney.getReceivingMoneyList()))
                .build();
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivingMoneyResponse {
        private BigDecimal receivingMoney;
        private Long userId;

        public static ReceivingMoneyResponse of(ReceivingMoney receivingMoney) {
            return ReceivingMoneyResponse.builder()
                    .receivingMoney(receivingMoney.getMoney())
                    .userId(receivingMoney.getUserId())
                    .build();
        }

        public static List<ReceivingMoneyResponse> from(List<ReceivingMoney> receivingMoneyList) {
            return receivingMoneyList.stream()
                    .map(ReceivingMoneyResponse::of)
                    .collect(Collectors.toList());
        }
    }
}
