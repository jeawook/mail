package com.system.mail.sendinfo;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static com.system.mail.sendinfo.QSendInfo.sendInfo;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class SendInfoRepositoryImpl implements  SendInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public SendInfo findByStatusAndSendTime(Status status, LocalDateTime sendDate) {
        return queryFactory.selectFrom(sendInfo)
                .where(statusEq(status), sendDateBefore(sendDate))
                .orderBy(sendInfo.sendDate.desc())
                .fetchFirst();
    }

    private BooleanExpression statusEq(Status status) {
        return status != null ? sendInfo.status.eq(status) : null;
    }

    private BooleanExpression sendDateBefore(LocalDateTime sendDate) {
        return sendDate != null ? sendInfo.sendDate.before(sendDate) : null;
    }

    @Override
    public Page<SendInfo> findByDateAndSubject(Pageable pageable, String subject, LocalDateTime startDate, LocalDateTime endDate) {
        QueryResults<SendInfo> results = queryFactory
                .selectFrom(sendInfo)
                .where(subjectContain(subject), dateBetween(startDate, endDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sendInfo.sendDate.desc())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null || endDate != null ? sendInfo.sendDate.between(startDate, endDate) : null;
    }


    private BooleanExpression subjectContain(String subject) {
        return hasText(subject) ? sendInfo.subject.contains(subject) : null;
    }

}
