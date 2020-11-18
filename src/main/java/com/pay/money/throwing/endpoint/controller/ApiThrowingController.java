package com.pay.money.throwing.endpoint.controller;

import com.pay.money.throwing.endpoint.controller.request.ThrowingMoneyRequest;
import com.pay.money.throwing.service.ThrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiThrowingController {

    private final ThrowingService throwingService;

    @PostMapping("/money/throwing")
    public String throwing(@RequestHeader("X-USER-ID") Long userId,
                           @RequestHeader("X-ROOM-ID") String roomId,
                           @RequestBody @Valid ThrowingMoneyRequest throwingMoneyRequest) {
        throwingMoneyRequest.validationThenException();
        String token = throwingService.save(userId, roomId, throwingMoneyRequest);

        return token;
    }


}
