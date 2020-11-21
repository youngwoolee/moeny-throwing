package com.pay.money.throwing.support;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyDistributor {
    List<BigDecimal> distribute(BigDecimal money, int personCount);
}

