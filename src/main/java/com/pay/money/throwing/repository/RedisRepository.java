package com.pay.money.throwing.repository;

import com.pay.money.throwing.error.ErrorCode;
import com.pay.money.throwing.error.exception.ApiSystemException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {

    private static final int EXPIRE_MINUTES = 10;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;

    public void set(String key, Object value) {
        valueOperations.set(key, value, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    public void setAllList(String key, List<Object> value) {
        listOperations.leftPushAll(key, value);
    }

    public Object getList(String key) {
        return listOperations.leftPop(key);
    }

    public void validateExpiredKey(final String key) {
        if (ObjectUtils.isEmpty(valueOperations.get(key))) {
            throw new ApiSystemException(ErrorCode.IS_EXPIRED_RECEIVE_DATE);
        }
    }
}
