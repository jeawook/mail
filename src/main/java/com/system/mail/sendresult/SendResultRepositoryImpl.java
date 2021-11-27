package com.system.mail.sendresult;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.system.mail.sendinfo.QSendInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.system.mail.sendinfo.QSendInfo.*;
import static com.system.mail.sendresult.QSendResult.*;

@RequiredArgsConstructor
public class SendResultRepositoryImpl implements SendResultRepositoryCustom {

    private final JPQLQueryFactory queryFactory;
    @Override
    public ResultInfoDto findSendResult(Long id) {
         return queryFactory
                .select(new QResultInfoDto(
                        sendResult.id.as("sendResultId"),
                        sendResult.totalCnt,
                        sendResult.errorCnt,
                        sendResult.successCnt,
                        sendResult.completedCnt,
                        sendResult.macroKey,
                        sendInfo.sendDate,
                        sendInfo.completedDate))
                .from(sendResult)
                .leftJoin(sendResult.sendInfo, sendInfo)
                .where(sendResult.id.eq(id))
                .fetchOne();
    }

    @Override
    public SendResult findBySendInfoId(Long sendInfoId) {
        return queryFactory
                .selectFrom(sendResult)
                .where(sendInfoIdEq(sendInfoId))
                .fetchOne();
    }

    private BooleanExpression sendInfoIdEq(Long sendInfoId) {
        return sendInfoId != null ? sendResult.sendInfo.id.eq(sendInfoId) : null;
    }
}
