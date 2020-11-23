package com.pay.money.throwing.service.pojo;

import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.error.exception.ApiSystemException;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserAndRoomDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String roomId;

    private UserAndRoomDto(final Long userId, final String roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public static UserAndRoomDto of(final Long userId, final String roomId) {
        return new UserAndRoomDto(userId, roomId);
    }

    public void validateNotSameUserAndSameRoom(final Long userId, final String roomId) {
        if(this.userId.equals(userId)) {
            throw new ApiSystemException(ErrorCode.CAN_RECEIVE_ONLY_OWNER);
        }

        if(!this.roomId.equals(roomId)) {
            throw new ApiSystemException(ErrorCode.CAN_RECEIVE_SAME_ROOM);
        }
    }

    public void validateSameUser(final Long userId) {
        if(!this.userId.equals(userId)) {
            throw new ApiSystemException(ErrorCode.CAN_SHOW_ONLY_OWNER);
        }
    }

}
