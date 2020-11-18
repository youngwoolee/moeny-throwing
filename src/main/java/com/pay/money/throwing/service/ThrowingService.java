package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ThrowingService {


    private final TokenGeneratorStrategy tokenGenerator;
    private final ThrowingRepository throwingRepository;

    public ThrowingService(@Qualifier("tokenGenerator") TokenGeneratorStrategy tokenGenerator, ThrowingRepository throwingRepository) {
        this.tokenGenerator = tokenGenerator;
        this.throwingRepository = throwingRepository;
    }

    private void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }


    public String save(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        String token = tokenGenerator.generate();
        save(throwingMoneyRequest.toEntity(userId, roomId, token));
        return token;
    }
}
