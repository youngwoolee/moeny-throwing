package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ReceivingMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceivingRepository extends JpaRepository<ReceivingMoney, Long> {

    ReceivingMoney findFirstByThrowingMoneyIdAndIsReceivedFalse(Long id);

    List<ReceivingMoney> findByThrowingMoneyIdAndIsReceivedTrue(Long id);
}

