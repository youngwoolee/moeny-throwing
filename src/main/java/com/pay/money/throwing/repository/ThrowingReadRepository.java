package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.pay.money.throwing.domain.QReceivingMoney.receivingMoney;
import static com.pay.money.throwing.domain.QThrowingMoney.throwingMoney;

@Repository
@RequiredArgsConstructor
public class ThrowingReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<ThrowingMoney> findByToken(String token) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(throwingMoney)
                .leftJoin(throwingMoney.receivingMoneyList, receivingMoney).fetchJoin()
                .where(throwingMoney.token.eq(token))
                .fetchOne());
    }
}
