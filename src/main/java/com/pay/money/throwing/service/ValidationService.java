package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private static final int EXPIRE_DAY = 7;

    public void receiveUser(Long userId, ThrowingMoney throwingMoney) {
        if(throwingMoney.isReceived(userId)) {
            throw new ApiSystemException(ErrorCode.IS_ALREADY_RECEIVE_USER);
        }
    }

    public void sameUserAndExpired(UserAndRoomDto userAndRoom, ThrowingMoney throwingMoney) {
        userAndRoom.validateSameUser(throwingMoney.getUserId());

        if(throwingMoney.isExpired(EXPIRE_DAY)) {
            throw new ApiSystemException(ErrorCode.IS_EXPIRED_SHOW_DATE);
        }
    }
}
