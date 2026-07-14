package com.system.mail.templete;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.system.mail.templete.QMailTemplate.*;

@RequiredArgsConstructor
public class MailTemplateRepositoryImpl implements MailTemplateRepositoryCustom {

    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<MailTemplate> searchByName(String searchKey, Pageable pageable) {
        QueryResults<MailTemplate> results = queryFactory
                .selectFrom(mailTemplate)
                .where(nameContain(searchKey))
                .orderBy(mailTemplate.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression nameContain(String searchKey) {
        return searchKey != null ? mailTemplate.templateName.contains(searchKey) : null;
    }
}
