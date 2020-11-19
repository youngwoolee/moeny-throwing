package com.pay.money.throwing.service;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.repository.ReceivingRepository;
import com.pay.money.throwing.repository.ThrowingRepository;
import com.pay.money.throwing.util.RandomMoneyDistributor;
import com.pay.money.throwing.util.TokenGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThrowingService {


    private final TokenGeneratorStrategy tokenGenerator;
    private final RandomMoneyDistributor randomDistributor;
    private final ThrowingRepository throwingRepository;
    private final ReceivingRepository receivingRepository;

    private void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }


    public String save(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        String token = tokenGenerator.generate();

        List<BigDecimal> distribute = randomDistributor.distribute(throwingMoneyRequest.getMoney(), throwingMoneyRequest.getPersonCount());

        ThrowingMoney throwingMoney = throwingMoneyRequest.toEntity(userId, roomId, token);

        distributeMoney(throwingMoney, distribute);

        save(throwingMoney);
        return token;
    }

    public void distributeMoney(ThrowingMoney throwingMoney, List<BigDecimal> distribute) {
        LocalDateTime updatedAt = LocalDateTime.now();
        for(BigDecimal money : distribute) {
            ReceivingMoney receivingMoney = ReceivingMoney.builder()
                    .roomId(throwingMoney.getRoomId())
                    .money(money)
                    .isReceived(false)
                    .updatedAt(updatedAt)
                    .throwingMoney(throwingMoney)
                    .build();
            throwingMoney.addReceivingMoney(receivingMoney);
        }
    }


    @Transactional
    public BigDecimal receiving(Long userId, String roomId, String token) {

        ThrowingMoney throwingMoney = throwingRepository.findByToken(token).orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        ReceivingMoney receivingMoney = receivingRepository.findFirstByThrowingMoneyIdAndIsReceivedFalse(throwingMoney.getId());

        if(ObjectUtils.isEmpty(receivingMoney)) {
            throw new RuntimeException("받을 돈이 없습니다");
        }
        receivingMoney.receiving(userId);
        return receivingMoney.getMoney();

    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {

        ThrowingMoney throwingMoney = throwingRepository.findByToken(token).orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        List<ReceivingMoney> receivingMoneyList = receivingRepository.findByThrowingMoneyIdAndIsReceivedTrue(throwingMoney.getId());

        ThrowingMoneyResponse throwingMoneyResponse = ThrowingMoneyResponse.valueOf(throwingMoney.getCreatedAt(), throwingMoney.getMoney(), receivingMoneyList);

        return throwingMoneyResponse;
    }
}
