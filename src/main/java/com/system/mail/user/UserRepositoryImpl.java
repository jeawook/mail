package com.system.mail.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.system.mail.user.QUser.user;
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public User findByIdAndGroupId(Long userId, Long groupId) {
        return jpaQueryFactory.selectFrom(user)
                .where(user.id.eq(userId))
                .where(user.mailGroup.id.eq(groupId))
                .fetchOne();
    }
}
