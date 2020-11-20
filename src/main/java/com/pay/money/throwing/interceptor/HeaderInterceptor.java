package com.pay.money.throwing.interceptor;

import com.pay.money.throwing.constant.HeaderName;
import com.pay.money.throwing.error.exception.ApiSystemException;
import com.pay.money.throwing.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    private static final String POST = "POST";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        boolean isEmptyUserId = ObjectUtils.isEmpty(request.getHeader(HeaderName.USER_ID));
        boolean isEmptyRoomId = ObjectUtils.isEmpty(request.getHeader(HeaderName.ROOM_ID));
        boolean isEmptyToken = false;
        
        if(!POST.equals(request.getMethod())) {
            isEmptyToken = ObjectUtils.isEmpty(request.getHeader(HeaderName.TOKEN));
        }

        if(isEmptyUserId || isEmptyRoomId || isEmptyToken) {
            throw new ApiSystemException(ErrorCode.IS_NOT_EXIST_NECESSARY_HEADER);
        }
        return true;
    }
}
