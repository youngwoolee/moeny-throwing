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
import org.springframework.util.ObjectUtils;

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
    private final RandomMoneyDistributor randomDistributor;
    private final ThrowingRepository throwingRepository;
    private final ReceivingRepository receivingRepository;
    private final ThrowingReadRepository throwingReadRepository;
    private final RedisService redisService;

    private void save(ThrowingMoney throwingMoney) {
        throwingRepository.save(throwingMoney);
    }


    public String save(Long userId, String roomId, ThrowingMoneyRequest throwingMoneyRequest) {
        String token = tokenGenerator.generate();

        List<BigDecimal> distribute = randomDistributor.distribute(throwingMoneyRequest.getMoney(), throwingMoneyRequest.getPersonCount());

        ThrowingMoney throwingMoney = throwingMoneyRequest.toEntity(userId, roomId, token);

//        distributeMoney(throwingMoney, distribute);

        List<ReceivingMoney> convert = convert(throwingMoney, distribute);

        ReceivingMoneyDto receivingMoneyDto = ReceivingMoneyDto.of(userId, roomId, distribute);

        save(throwingMoney);

        //TODO: redis key: token, value : userId, roomId, List<BigDecimal>
        redisService.set(token, receivingMoneyDto);
        return token;
    }

    private List<ReceivingMoney> convert(ThrowingMoney throwingMoney, List<BigDecimal> distribute) {
        List<ReceivingMoney> receivingMoneyList = new ArrayList<>();
        LocalDateTime updatedAt = LocalDateTime.now();
        for(BigDecimal money : distribute) {
            ReceivingMoney receivingMoney = ReceivingMoney.builder()
                    .roomId(throwingMoney.getRoomId())
                    .money(money)
                    .throwingMoney(throwingMoney)
                    .build();
            receivingMoneyList.add(receivingMoney);
        }
        return receivingMoneyList;
    }

    public void distributeMoney(ThrowingMoney throwingMoney, List<BigDecimal> distribute) {
        LocalDateTime updatedAt = LocalDateTime.now();
        for(BigDecimal money : distribute) {
            ReceivingMoney receivingMoney = ReceivingMoney.builder()
                    .roomId(throwingMoney.getRoomId())
                    .money(money)
                    .updatedAt(updatedAt)
                    .throwingMoney(throwingMoney)
                    .build();
            throwingMoney.addReceivingMoney(receivingMoney);
        }
    }


    @Transactional
    public BigDecimal receiving(Long userId, String roomId, String token) {

        redisService.validateExpiredKey(token);
        //TODO: 자기자신은 받을수 없음
        //TODO: 동일한 방 인원만 받을 수있음
        ReceivingMoneyDto receivingMoneyDto = redisService.get(token);
        receivingMoneyDto.validateNotSameUserAndSameRoom(userId, roomId);

        //TODO: 조인결과를 가져와야함
        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token).orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        ReceivingMoney receivingMoney = ReceivingMoney.builder()
                .roomId(throwingMoney.getRoomId())
                .money(receivingMoneyDto.getDistributeMoney(1))
                .userId(userId)
                .updatedAt(LocalDateTime.now())
                .throwingMoney(throwingMoney)
                .build();

        receivingRepository.save(receivingMoney);
        //TODO: 한 사용자는 한번만 받을수 있음

        return receivingMoney.getMoney();
    }

    public ThrowingMoneyResponse show(Long userId, String roomId, String token) {

        ThrowingMoney throwingMoney = throwingReadRepository.findByToken(token).orElseThrow(() -> new RuntimeException("해당 뿌리기 건이 없습니다"));

        //TODO: 자기 자신만 조회 가능
        if(!throwingMoney.isSameUser(userId)) {
            throw new RuntimeException("본인만 조회 가능합니다");
        }
        //TODO: 7일동안만 조회 가능
        if(throwingMoney.isExpired(EXPIRE_DAY)) {
            throw new RuntimeException("조회 만료 일자가 지났습니다");
        }

        ThrowingMoneyResponse throwingMoneyResponse = ThrowingMoneyResponse.valueOf(throwingMoney.getCreatedAt(), throwingMoney.getMoney(), throwingMoney.getReceivingMoneyList());

        return throwingMoneyResponse;
    }
}
