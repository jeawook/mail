package com.system.mail.sendinfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static com.system.mail.sendinfo.QSendInfo.sendInfo;

@RequiredArgsConstructor
public class SendInfoRepositoryImpl implements  SendInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public SendInfo findByStatusWait() {
        return queryFactory.selectFrom(sendInfo)
                .where(sendInfo.status.eq(Status.WAIT))
                .where(sendInfo.sendDate.before(LocalDateTime.now())).fetchOne();

    }

}
