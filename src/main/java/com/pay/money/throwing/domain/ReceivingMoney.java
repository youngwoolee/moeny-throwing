package com.pay.money.throwing.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(indexes = {@Index(name = "throw_money_id_user_index",unique = true, columnList="throwing_money_id, userId")})
public class ReceivingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private BigDecimal money;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "throwing_money_id")
    private ThrowingMoney throwingMoney;

    @Builder
    public ReceivingMoney(String roomId, BigDecimal money, Long userId, LocalDateTime createdAt, ThrowingMoney throwingMoney) {
        this.roomId = roomId;
        this.money = money;
        this.userId = userId;
        this.createdAt = createdAt;
        this.throwingMoney = throwingMoney;
    }

    public static ReceivingMoney of(Long userId, String roomId, BigDecimal distributeMoney, ThrowingMoney throwingMoney) {
        return ReceivingMoney.builder()
                .roomId(roomId)
                .money(distributeMoney)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .throwingMoney(throwingMoney)
                .build();
    }
}
