package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.repository.ReceivingRepository;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.service.pojo.ReceivingMoneyDto;
import com.pay.money.throwing.util.RandomMoneyDistributor;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThrowingService {

    private static final int EXPIRE_DAY = 7;

    private final TokenGeneratorStrategy tokenGenerator;
    private final ThrowingRepository throwingRepository;
    private final ReceivingRepository receivingRepository;
    private final ThrowingReadRepository throwingReadRepository;
    private final RedisService redisService;

    public String throwing(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        //TODO: token 중복 체크
        String token = createToken();

        save(throwingMoneyRequest.toEntity(userId, roomId, token));

        redisService.setDistributeMoney(token, userId, roomId, throwingMoneyRequest);
        return token;
    }

    private String createToken() {
        return tokenGenerator.generate();
    }

    private void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }


    @Transactional
    public BigDecimal receiving(Long userId, String roomId, String token) {

        redisService.validateExpiredKey(token);

        ReceivingMoneyDto receivingMoneyDto = redisService.getReceivingMoneyDto(userId, roomId, token);

        ThrowingMoney findThrowingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        if(findThrowingMoney.isReceived(userId)) {
            throw new RuntimeException("이미 받은 사용자입니다");
        }

        ReceivingMoney saveReceivingMoney = receivingMoneyDto.toEntity(userId, roomId, findThrowingMoney);

        findThrowingMoney.addReceivingMoney(saveReceivingMoney);

//        receivingRepository.save(saveReceivingMoney);

        return saveReceivingMoney.getMoney();
    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {

        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        if(!throwingMoney.isSameUser(userId)) {
            throw new RuntimeException("본인만 조회 가능합니다");
        }

        if(throwingMoney.isExpired(EXPIRE_DAY)) {
            throw new RuntimeException("조회 만료 일자가 지났습니다");
        }

        return ThrowingMoneyResponse.of(throwingMoney);
    }
}
