package com.pay.money.throwing.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class ThrowingMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


}
