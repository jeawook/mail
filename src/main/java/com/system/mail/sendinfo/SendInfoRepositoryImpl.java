package com.system.mail.sendinfo;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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
        List<SendInfo> fetch = queryFactory
                .selectFrom(sendInfo)
                .where(subjectContain(subject), dateBetween(startDate, endDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sendInfo.sendDate.desc())
                .fetch();

        return new PageImpl<>(fetch, pageable, fetch.size());
    }

    @Override
    public SendInfoDto findSendInfoDtoById(Long id) {
        return queryFactory
                .select(new QSendInfoDto(
                        sendInfo.id,
                        sendInfo.sendDate,
                        sendInfo.completedDate,
                        sendInfo.mailInfo.mailInfoName,
                        sendInfo.mailGroup.mailGroupName,
                        sendInfo.status,
                        sendInfo.subject,
                        sendInfo.content,
                        sendInfo.sendResult.id.as("sendResultId")
                ))
                .from(sendInfo)
                .where(idEq(id))
                .fetchOne();
    }

    private BooleanExpression idEq(Long id) {
        return id != null ? sendInfo.id.eq(id) : null;
    }

    private BooleanExpression dateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null || endDate != null ? sendInfo.sendDate.between(startDate, endDate) : null;
    }


    private BooleanExpression subjectContain(String subject) {
        return hasText(subject) ? sendInfo.subject.contains(subject) : null;
    }

}
