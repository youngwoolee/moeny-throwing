package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.repository.ReceivingRepository;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceivingService {

    private final ReceivingRepository receivingRepository;
    private final DistributeService distributeService;
    private final ThrowingReadRepository throwingReadRepository;
    private final ValidationService validationService;

    @Transactional
    public BigDecimal receiving(Long userId, String roomId, String token) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        distributeService.validateExpiredKey(token);

        distributeService.validateUserAndRoom(userId, roomId, token);

        ThrowingMoney findThrowingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        validationService.receiveUser(userId, findThrowingMoney);

        ReceivingMoney distributeReceiveMoney = distributeService.getDistributeReceiveMoney(userAndRoom, token, findThrowingMoney);

        ReceivingMoney saveReceivingMoney = receivingRepository.save(distributeReceiveMoney);
        log.info("success receiving money [userId: {}, roomId: {}, money: {}]", saveReceivingMoney.getUserId(), saveReceivingMoney.getRoomId(), saveReceivingMoney.getMoney());
        return distributeReceiveMoney.getMoney();
    }
}
