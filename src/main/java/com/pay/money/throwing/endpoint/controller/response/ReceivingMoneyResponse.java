package com.pay.money.throwing.endpoint.controller.response;

import com.pay.money.throwing.domain.ReceivingMoney;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ReceivingMoneyResponse {
//    private BigDecimal receivingMoney;
//    private Long userId;
//
//    public static ReceivingMoneyResponse of(ReceivingMoney receivingMoney) {
//        return ReceivingMoneyResponse.builder()
//                .receivingMoney(receivingMoney.getMoney())
//                .userId(receivingMoney.getUserId())
//                .build();
//    }
//
//    public static List<ReceivingMoneyResponse> from(List<ReceivingMoney> receivingMoneyList) {
//        return receivingMoneyList.stream()
//                .map(ReceivingMoneyResponse::of)
//                .collect(Collectors.toList());
//    }
//}
