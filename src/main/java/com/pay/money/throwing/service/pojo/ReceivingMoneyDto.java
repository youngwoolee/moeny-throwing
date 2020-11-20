package com.pay.money.throwing.service.pojo;

import com.pay.money.throwing.domain.ReceivingMoney;
import com.pay.money.throwing.domain.ThrowingMoney;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.error.ErrorCode;
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
            throw new ApiSystemException(ErrorCode.CAN_RECEIVE_ONLY_OWNER);
        }

        if(!this.roomId.equals(roomId)) {
            throw new ApiSystemException(ErrorCode.CAN_RECEIVE_SAME_ROOM);
        }
    }

    public BigDecimal getDistributeMoney(int sequence) {
        if(distributeMoneyList.isEmpty() || distributeMoneyList.size() <= sequence) {
            throw new ApiSystemException(ErrorCode.IS_NOT_ENOUGH_DISTRIBUTE_MONEY);
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
