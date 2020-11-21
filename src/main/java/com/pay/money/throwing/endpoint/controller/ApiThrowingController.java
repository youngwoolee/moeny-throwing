package com.pay.money.throwing.endpoint.controller;

import com.pay.money.throwing.constant.HeaderName;
import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.endpoint.controller.response.ThrowingMoneyResponse;
import com.pay.money.throwing.service.ThrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiThrowingController {


    private final ThrowingService throwingService;

    @PostMapping("/money/throwing")
    public String throwing(@RequestHeader(HeaderName.USER_ID) Long userId,
                           @RequestHeader(HeaderName.ROOM_ID) String roomId,
                           @RequestBody @Valid ThrowingMoneyRequest throwingMoneyRequest) {
        String token = throwingService.throwing(userId, roomId, throwingMoneyRequest);

        return token;
    }

    @PatchMapping("/money/throwing")
    public BigDecimal receiving(@RequestHeader(HeaderName.USER_ID) Long userId,
                                @RequestHeader(HeaderName.ROOM_ID) String roomId,
                                @RequestHeader(HeaderName.TOKEN) String token) {

        BigDecimal money = throwingService.receiving(userId, roomId, token);
        return money;
    }

    @GetMapping("/money/throwing")
    public ThrowingMoneyResponse show(@RequestHeader(HeaderName.USER_ID) Long userId,
                                @RequestHeader(HeaderName.ROOM_ID) String roomId,
                                @RequestHeader(HeaderName.TOKEN) String token) {

        ThrowingMoneyResponse throwingMoneyResponse = throwingService.show(userId, roomId, token);

        return throwingMoneyResponse;
    }


}
