package com.pay.money.throwing.service.pojo;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReceivingMoneyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String roomId;
    private List<BigDecimal> distributeMoneyList;

    private ReceivingMoneyDto(final Long userId, final String roomId, final List<BigDecimal> distributeMoneyList) {
        this.userId = userId;
        this.roomId = roomId;
        this.distributeMoneyList = distributeMoneyList;
    }

    public static ReceivingMoneyDto of(final Long userId, final String roomId, final List<BigDecimal> distributeMoneyList) {
        return new ReceivingMoneyDto(userId, roomId, distributeMoneyList);
    }

    public void validateNotSameUserAndSameRoom(Long userId, String roomId) {
        if(this.userId.equals(userId)) {
            throw new RuntimeException("뿌린사람은 받을 수 없습니다");
        }

        if(!this.roomId.equals(roomId)) {
            throw new RuntimeException("같은 방이 아니면 받을 수 없습니다");
        }
    }

    public BigDecimal getDistributeMoney(int sequence) {
        if(distributeMoneyList.isEmpty() || distributeMoneyList.size() <= sequence) {
            throw new RuntimeException("분배할 돈이 없습니다");
        }
        return distributeMoneyList.get(sequence);
    }

    public ReceivingMoney toEntity(Long userId, String roomId, ThrowingMoney throwingMoney) {
        List<ReceivingMoney> receivingMoneyList = throwingMoney.getReceivingMoneyList();
        int sequence = receivingMoneyList.size();
        return ReceivingMoney.builder()
                .roomId(roomId)
                .money(this.getDistributeMoney(sequence))
                .userId(userId)
                .updatedAt(LocalDateTime.now())
                .throwingMoney(throwingMoney)
                .build();
    }
}
