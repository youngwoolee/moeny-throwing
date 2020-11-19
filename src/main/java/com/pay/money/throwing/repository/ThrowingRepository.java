package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ThrowingMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThrowingRepository extends JpaRepository<ThrowingMoney, Long> {

    Optional<ThrowingMoney> findByToken(String token);

}
