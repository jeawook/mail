package com.system.mail.sendinfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.system.mail.sendinfo.QSendInfo.sendInfo;

@RequiredArgsConstructor
public class SendInfoRepositoryImpl implements  SendInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<SendInfo> findByStatusAndSendTime(Status status, LocalDateTime localDateTime) {
        return Optional.ofNullable(queryFactory.selectFrom(sendInfo)
                .where(sendInfo.status.eq(status))
                .where(sendInfo.sendDate.before(localDateTime)).fetchOne());
    }

    @Override
    public Page<SendInfo> findByDateAndSubject(LocalDateTime data, String subject, Pageable pageable) {
        return null;
    }

}
