package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.repository.ReceivingRepository;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReceivingService {

    private final ReceivingRepository receivingRepository;
    private final DistributeService distributeService;
    private final ThrowingReadRepository throwingReadRepository;
    private final ValidationService validationService;

    @Transactional
    public void save(ReceivingMoney receivingMoney) {
        receivingRepository.save(receivingMoney);
    }

    public BigDecimal receiving(Long userId, String roomId, String token) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        distributeService.validateExpiredKey(token);

        distributeService.validateUserAndRoom(userId, roomId, token);

        ThrowingMoney findThrowingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        validationService.receiveUser(userId, findThrowingMoney);

        ReceivingMoney distributeReceiveMoney = distributeService.getDistributeReceiveMoney(userAndRoom, token, findThrowingMoney);

        save(distributeReceiveMoney);

        return distributeReceiveMoney.getMoney();
    }
}
