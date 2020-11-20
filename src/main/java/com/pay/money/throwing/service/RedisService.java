package com.pay.money.throwing.service;

import com.pay.money.throwing.service.pojo.ReceivingMoneyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final int EXPIRE_MINUTES = 30;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(key, value, EXPIRE_MINUTES, TimeUnit.SECONDS);
    }

    public ReceivingMoneyDto get(String key) {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        ReceivingMoneyDto receivingMoneyDto = (ReceivingMoneyDto) vop.get(key);
        return receivingMoneyDto;
    }

    public void validateExpiredKey(final String key) {
        if (ObjectUtils.isEmpty(redisTemplate.opsForValue().get(key))) {
            throw new RuntimeException("해당 뿌린돈이 유효 하지 않습니다");
        }
    }

}
