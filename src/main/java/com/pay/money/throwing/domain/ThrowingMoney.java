package com.pay.money.throwing.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class ThrowingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private BigDecimal money;

    @Column(nullable = false)
    private int personCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isExpired;

    @OneToMany(mappedBy = "throwingMoney", cascade = CascadeType.ALL)
    private List<ReceivingMoney> receivingMoneyList = new ArrayList<>();


    @Builder
    public ThrowingMoney(Long userId, String roomId, String token, BigDecimal money, int personCount) {
        this.userId = userId;
        this.roomId = roomId;
        this.token = token;
        this.money = money;
        this.personCount = personCount;
        this.createdAt = LocalDateTime.now();
        this.isExpired = false;
    }

    public void addReceivingMoney(ReceivingMoney receivingMoneyList) {
        this.receivingMoneyList.add(receivingMoneyList);
    }
}
