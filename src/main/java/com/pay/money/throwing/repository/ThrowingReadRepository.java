package com.pay.money.throwing.repository;

import com.pay.money.throwing.domain.ThrowingMoney;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.pay.money.throwing.domain.QReceivingMoney.receivingMoney;
import static com.pay.money.throwing.domain.QThrowingMoney.throwingMoney;

@Repository
public class ThrowingReadRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ThrowingReadRepository(JPAQueryFactory jpaQueryFactory) {
        super(ThrowingMoney.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<ThrowingMoney> findByToken(String token) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(throwingMoney)
                .leftJoin(throwingMoney.receivingMoneyList, receivingMoney).fetchJoin()
                .where(throwingMoney.token.eq(token))
                .fetchOne());
    }
}
