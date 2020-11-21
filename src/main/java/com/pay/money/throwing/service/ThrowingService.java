package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.repository.ThrowingReadRepository;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.service.pojo.UserAndRoomDto;
import com.pay.money.throwing.support.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ThrowingService {

    private final TokenGeneratorStrategy tokenGenerator;
    private final ThrowingRepository throwingRepository;
    private final ThrowingReadRepository throwingReadRepository;
    private final DistributeService distributeService;
    private final ValidationService validationService;

    public String throwing(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        //TODO: token 중복 체크
        String token = createToken();

        distributeService.saveUserAndRoomAndDistributeMoney(token, userAndRoom, throwingMoneyRequest);

        save(throwingMoneyRequest.toEntity(userId, roomId, token));
        return token;
    }

    private String createToken() {
        return tokenGenerator.generate();
    }

    @Transactional
    public void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {
        UserAndRoomDto userAndRoom = UserAndRoomDto.of(userId, roomId);

        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token)
                .orElseThrow(ErrorCode.IS_NOT_EXIST_THROWING_MONEY::exception);

        validationService.sameUserAndExpired(userAndRoom, throwingMoney);

        return ThrowingMoneyResponse.of(throwingMoney);
    }

}
