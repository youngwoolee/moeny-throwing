package com.pay.money.throwing.util;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyDistributor {
    List<BigDecimal> distribute(BigDecimal money, int personCount);
}

