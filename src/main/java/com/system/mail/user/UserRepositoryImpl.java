package com.system.mail.user;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.system.mail.user.QUser.user;
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public User findByIdAndGroupId(Long userId, Long groupId) {
        return queryFactory.selectFrom(user)
                .where(user.id.eq(userId))
                .where(groupIdEq(groupId))
                .fetchOne();
    }

    private BooleanExpression groupIdEq(Long groupId) {
        return groupId != null ? user.mailGroup.id.eq(groupId) : null;
    }

    @Override
    public Page<User> findByGroupId(Long groupId, Pageable pageable) {
        QueryResults<User> results = queryFactory
                .selectFrom(user)
                .where(groupIdEq(groupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.id.desc())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
