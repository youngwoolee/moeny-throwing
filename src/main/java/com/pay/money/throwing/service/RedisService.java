package com.pay.money.throwing.service;

import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.repository.RedisRepository;
import com.pay.money.throwing.service.pojo.ReceivingMoneyDto;
import com.pay.money.throwing.support.RandomMoneyDistributor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;
    private final RandomMoneyDistributor randomDistributor;

    public void validateExpiredKey(final String key) {
        redisRepository.validateExpiredKey(key);
    }

    public void setDistributeMoney(String token, Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        List<BigDecimal> distributeMoney = randomDistributor.distribute(throwingMoneyRequest.getMoney(), throwingMoneyRequest.getPersonCount());
        ReceivingMoneyDto receivingMoneyDto = ReceivingMoneyDto.of(userId, roomId, distributeMoney);
        redisRepository.set(token, receivingMoneyDto);
    }

    public ReceivingMoneyDto getReceivingMoneyDto(Long userId, String roomId, String token) {
        ReceivingMoneyDto receivingMoneyDto = redisRepository.get(token);
        receivingMoneyDto.validateNotSameUserAndSameRoom(userId, roomId);
        return receivingMoneyDto;
    }
}
