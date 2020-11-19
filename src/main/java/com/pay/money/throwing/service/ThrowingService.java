package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.util.RandomMoneyDistributor;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ThrowingService {


    private final TokenGeneratorStrategy tokenGenerator;
    private final RandomMoneyDistributor randomDistributor;
    private final ThrowingRepository throwingRepository;

    private void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }


    public String save(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        String token = tokenGenerator.generate();

        List<BigDecimal> distribute = randomDistributor.distribute(throwingMoneyRequest.getMoney(), throwingMoneyRequest.getPersonCount());

        ThrowingMoney throwingMoney = throwingMoneyRequest.toEntity(userId, roomId, token);

        distributeMoney(throwingMoney, distribute);

        save(throwingMoney);
        return token;
    }

    public void distributeMoney(ThrowingMoney throwingMoney, List<BigDecimal> distribute) {
        AtomicInteger atomic = new AtomicInteger(0);
        LocalDateTime updatedAt = LocalDateTime.now();
        for(BigDecimal money : distribute) {
            ReceivingMoney receivingMoney = ReceivingMoney.builder()
                    .roomId(throwingMoney.getRoomId())
                    .money(money)
                    .isReceived(false)
                    .updatedAt(updatedAt)
                    .sequence(atomic.incrementAndGet())
                    .throwingMoney(throwingMoney)
                    .build();
            throwingMoney.addReceivingMoney(receivingMoney);
        }
    }




}
