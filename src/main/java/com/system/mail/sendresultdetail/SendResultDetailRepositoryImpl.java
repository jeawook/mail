package com.system.mail.sendresultdetail;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.system.mail.sendresultdetail.QSendResultDetail.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class SendResultDetailRepositoryImpl implements SendResultDetailRepositoryCustom {

    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<SendResultDetail> findByResultId(Long resultId, Pageable pageable) {
        QueryResults<SendResultDetail> results = queryFactory
                .selectFrom(sendResultDetail)
                .where(resultIdEq(resultId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sendResultDetail.id.desc())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression resultIdEq(Long resultId) {
        return resultId != null ? sendResultDetail.sendResult.id.eq(resultId) : null;
    }

    @Override
    public Page<SendResultDetail> findByNameOrEmail(Long resultId, SendResultDetailSearchCond searchCond, Pageable pageable) {
        QueryResults<SendResultDetail> results = queryFactory
                .selectFrom(sendResultDetail)
                .where(searchable(searchCond.getType(), searchCond.getValue()) , resultIdEq(resultId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sendResultDetail.id.desc())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression searchable( ReusltSearchType searchType, String value) {
        if (searchType.equals(ReusltSearchType.NAME)) {
            return mailAddressNameContain(value);
        } else if (searchType.equals(ReusltSearchType.EMAIL)) {
            return mailAddressEmailContain(value);
        } else {
            return mailAddressNameContain(value).or(mailAddressEmailContain(value));
        }

    }

    private BooleanExpression mailAddressEmailContain(String email) {
        return hasText(email) ? sendResultDetail.mailAddress.email.contains(email) : null;
    }

    private BooleanExpression mailAddressNameContain(String name) {
        return hasText(name) ? sendResultDetail.mailAddress.name.contains(name) : null;
    }
}
