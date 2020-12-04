package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import com.pay.money.throwing.support.TokenGeneratorStrategy;
import com.sun.xml.internal.ws.api.model.CheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleUnresolved;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThrowingService {

    private final TokenGeneratorStrategy tokenGenerator;
    private final ThrowingRepository throwingRepository;
    private final ThrowingReadRepository throwingReadRepository;
    private final DistributeService distributeService;
    private final ValidationService validationService;

    @Transactional
    public String throwing(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        String token = createToken();

        distributeService.saveUserAndRoomAndDistributeMoney(token, userAndRoom, throwingMoneyRequest);

        ThrowingMoney saveThrowingMoney = throwingRepository.save(throwingMoneyRequest.toEntity(userId, roomId, token));
        log.info("success throwing money [userId: {}, roomId: {}, money: {}]", saveThrowingMoney.getUserId(), saveThrowingMoney.getRoomId(), saveThrowingMoney.getMoney());
        return token;
    }

    private String createToken() {
        return tokenGenerator.generate();
    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        validationService.sameUserAndExpired(userAndRoom, throwingMoney);

        return ThrowingMoneyResponse.of(throwingMoney);
    }

}
