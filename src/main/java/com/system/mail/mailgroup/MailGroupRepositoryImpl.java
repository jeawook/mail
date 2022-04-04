package com.system.mail.mailgroup;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.system.mail.mailgroup.QMailGroup.mailGroup;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MailGroupRepositoryImpl implements MailGroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<MailGroup> searchByName(String searchKey, Pageable pageable) {
        List<MailGroup> fetch = queryFactory
                .selectFrom(mailGroup)
                .where(nameContain(searchKey))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(mailGroup.id.desc())
                .fetch();
        return new PageImpl<>(fetch,pageable, fetch.size() );
    }

    private BooleanExpression nameContain(String name) {
        return hasText(name) ? mailGroup.mailGroupName.contains(name) : null;
    }
}
