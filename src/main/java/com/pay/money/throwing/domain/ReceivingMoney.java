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
public class ReceivingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private BigDecimal money;

    private Long userId;

    @Column(nullable = false)
    private boolean isReceived;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "throwing_money_id")
    private ThrowingMoney throwingMoney;

    @Builder
    public ReceivingMoney(String roomId, BigDecimal money, boolean isReceived, LocalDateTime updatedAt, ThrowingMoney throwingMoney) {
        this.roomId = roomId;
        this.money = money;
        this.isReceived = isReceived;
        this.updatedAt = updatedAt;
        this.throwingMoney = throwingMoney;
    }

    public void receiving(Long userId) {
        this.userId = userId;
        this.isReceived = true;
        this.updatedAt = LocalDateTime.now();
    }
}
