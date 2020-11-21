package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.repository.RedisRepository;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import com.pay.money.throwing.support.RandomMoneyDistributor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributeService {

    private final RedisRepository redisRepository;
    private final RandomMoneyDistributor randomDistributor;
    private static final String TOKEN_MONEY_KEY = ":moneys";

    public void validateExpiredKey(final String key) {
        redisRepository.validateExpiredKey(key);
    }

    public void saveUserAndRoomAndDistributeMoney(String token, UserAndRoomDto userAndRoom, ThrowingMoneyRequest throwingMoneyRequest) {
        List<BigDecimal> distributeMoney = randomDistributor.distribute(throwingMoneyRequest.getMoney(), throwingMoneyRequest.getPersonCount());
        redisRepository.set(token, userAndRoom);
        redisRepository.setAllList(token + TOKEN_MONEY_KEY, new ArrayList<>(distributeMoney));
    }

    public ReceivingMoney getDistributeReceiveMoney(UserAndRoomDto userAndRoom, String token, ThrowingMoney findThrowingMoney) {
        Object distributeMoney = redisRepository.getList(token + TOKEN_MONEY_KEY);
        if(ObjectUtils.isEmpty(distributeMoney)) {
            throw new ApiSystemException(ErrorCode.IS_NOT_ENOUGH_DISTRIBUTE_MONEY);
        }
        return ReceivingMoney.of(userAndRoom.getUserId(), userAndRoom.getRoomId(), (BigDecimal) distributeMoney, findThrowingMoney);
    }

    public void validateUserAndRoom(Long userId, String roomId, String token) {
        UserAndRoomDto userAndRoom = (UserAndRoomDto) redisRepository.get(token);
        userAndRoom.validateNotSameUserAndSameRoom(userId, roomId);
    }
}
