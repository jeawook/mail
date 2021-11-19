package com.system.mail.mailinfo;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.system.mail.mailinfo.QMailInfo.*;

@RequiredArgsConstructor
public class MailInfoRepositoryImpl implements MailInfoRepositoryCustom {

    private final JPQLQueryFactory queryFactory;
    @Override
    public Page<MailInfo> searchByName(String searchKey, Pageable pageable) {
        QueryResults<MailInfo> results = queryFactory
                .selectFrom(mailInfo)
                .where(nameContain(searchKey))
                .orderBy(mailInfo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression nameContain(String searchKey) {
        return searchKey != null ? mailInfo.mailInfoName.contains(searchKey) : null;
    }
}
