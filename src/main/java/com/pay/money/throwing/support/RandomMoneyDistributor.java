package com.pay.money.throwing.support;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;

public class RandomMoneyDistributor implements MoneyDistributor {

    @Override
    public List<BigDecimal> distribute(BigDecimal money, int personCount) {
        Random random = new Random();
        long[] rateArr = new long[personCount];
        long moneyToLong = money.longValue();

        int sum = 0;
        for (int i = 0; i < personCount - 1; i++) {
            int bound = (int)(moneyToLong - sum) / 2;
            rateArr[i] = bound >= 1 ?  random.nextInt(bound) + 1 : 0L;
            sum += rateArr[i];
        }
        rateArr[personCount-1] = moneyToLong - sum;

        return Arrays.stream(rateArr).mapToObj(BigDecimal::valueOf).collect(Collectors.toList());
    }

}
