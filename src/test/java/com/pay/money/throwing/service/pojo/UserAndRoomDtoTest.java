package com.pay.money.throwing.service.pojo;

import com.pay.money.throwing.error.exception.ApiSystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserAndRoomDtoTest {

    private UserAndRoomDto userAndRoom;
    private Long userId;
    private String roomId;

    @BeforeEach
    public void setUp() {
        userAndRoom = UserAndRoomDto.of(1L, "abc");
        userId = 1L;
        roomId = "abc";
    }

    @DisplayName("동일한 유저, 방인지 검사 : 실패")
    @Test
    void validateNotSameUserAndSameRoomTest() {
        assertThatThrownBy(() -> userAndRoom.validateNotSameUserAndSameRoom(userId, roomId))
                .isInstanceOf(ApiSystemException.class);
    }

    @DisplayName("동일한 유저아닌지 검사 : 성공")
    @Test
    void validateSameUser() {
        assertDoesNotThrow(() -> userAndRoom.validateSameUser(userId));
    }
}