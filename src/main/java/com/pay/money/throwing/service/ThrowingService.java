package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.repository.ReceivingRepository;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.service.pojo.ReceivingMoneyDto;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        if(findThrowingMoney.isReceived(userId)) {
            throw new ApiSystemException(ErrorCode.IS_ALREADY_RECEIVE_USER);
        }

        ReceivingMoney saveReceivingMoney = receivingMoneyDto.toEntity(userId, roomId, findThrowingMoney);

        findThrowingMoney.addReceivingMoney(saveReceivingMoney);

        return saveReceivingMoney.getMoney();
    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {

        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        if(!throwingMoney.isSameUser(userId)) {
            throw new ApiSystemException(ErrorCode.CAN_SHOW_ONLY_OWNER);
        }

        if(throwingMoney.isExpired(EXPIRE_DAY)) {
            throw new ApiSystemException(ErrorCode.IS_EXPIRED_SHOW_DATE);
        }

        return ThrowingMoneyResponse.of(throwingMoney);
    }
}
