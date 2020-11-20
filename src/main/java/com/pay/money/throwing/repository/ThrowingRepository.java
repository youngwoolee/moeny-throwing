package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ThrowingMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThrowingRepository extends JpaRepository<ThrowingMoney, Long> {

}
