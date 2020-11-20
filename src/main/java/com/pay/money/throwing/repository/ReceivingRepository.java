package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ReceivingMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceivingRepository extends JpaRepository<ReceivingMoney, Long> {

    ReceivingMoney findFirstByThrowingMoneyId(Long id);

    List<ReceivingMoney> findByThrowingMoneyId(Long id);
}

