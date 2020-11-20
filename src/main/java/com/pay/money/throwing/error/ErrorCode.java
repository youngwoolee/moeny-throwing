package com.pay.money.throwing.error;

import com.pay.money.throwing.error.exception.ApiSystemException;
import lombok.Getter;

@Getter
public enum ErrorCode {
    IS_MONEY_LESS_THAN_ZERO("999", "뿌리는 돈이 0 이상 이어야한다"),
    IS_PERSON_COUNT_GREATER_THAN_ZERO("998", "받을 인원이 0 이상 이어야한다"),
    IS_NOT_EXIST_NECESSARY_HEADER("997", "필수 헤더값이 없습니다"),
    CAN_RECEIVE_ONLY_OWNER("996", "뿌리기를 한사람은 받을 수 없습니다"),
    CAN_RECEIVE_SAME_ROOM("995", "해당 방 사용자가 아니면 받을 수 없습니다"),
    IS_NOT_ENOUGH_DISTRIBUTE_MONEY("994", "분배할수 있는 돈이 없습니다"),
    IS_EXPIRED_RECEIVE_DATE("993", "받을 수 있는 날짜가 만료됐습니다"),
    IS_NOT_EXIST_THROWING_MONEY("992", "해당 뿌리기 건이 없습니다"),
    IS_ALREADY_RECEIVE_USER("991", "이미 받은 사용자입니다"),
    CAN_SHOW_ONLY_OWNER("990", "본인만 조회 가능합니다"),
    IS_EXPIRED_SHOW_DATE("989", "조회 만료 일자가 지났습니다");

    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public ApiSystemException exception() {
        return new ApiSystemException(this);
    }

}
