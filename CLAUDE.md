# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 언어 설정

Claude는 이 저장소에서 작업할 때 사용자와 항상 한글로 응답한다.

## 프로젝트 개요

Spring Boot 기반 메일 대량발송 관리 시스템(SystemMail). JavaMail 같은 라이브러리를 쓰지 않고, DNS(MX) lookup으로 수신 서버 주소를 직접 조회한 뒤 소켓으로 SMTP 프로토콜을 구현해 메일을 발송한다. 발송은 UI(Thymeleaf)에서 등록하고, 실제 발송은 스케줄러가 백그라운드에서 처리한다.

## 빌드 / 테스트 명령어

Windows 환경이므로 `gradlew.bat`을 사용한다.

```
gradlew.bat build              # 빌드 (querydsl 소스 생성 포함)
gradlew.bat test                # 전체 테스트 실행 (JUnit 5)
gradlew.bat test --tests "com.system.mail.mailprocessor.MailProcessorTest"          # 클래스 단위 테스트
gradlew.bat test --tests "com.system.mail.mailprocessor.MailProcessorTest.mailSendTest"  # 단일 테스트 메서드
gradlew.bat bootRun             # 애플리케이션 실행 (기본 포트 8080)
```

QueryDSL Q타입은 Gradle 표준 `annotationProcessor`(`com.querydsl:querydsl-apt::jpa`)로 `compileJava` 과정 중에 `build/generated/sources/annotationProcessor/java/main`에 생성된다(과거에 쓰던 `com.ewerk.gradle.plugins:querydsl-plugin`은 2018년 이후 업데이트가 없어 제거함). 별도 `compileQuerydsl` 태스크가 없으므로 `gradlew.bat compileJava`(또는 `build`/`bootRun`/`test`) 한 번이면 Q클래스가 함께 생성된다.

## 사전 설정

- `src/main/resources/application.yml`에 MySQL 접속 정보(`systemmail` 스키마), `mail.domain`(발송 도메인), `mail.propertyMap`(returnPath, DKIM-Signature 등 SMTP 헤더), `domain.domainConnectionInfo`(도메인별 동시 연결 수 제한, `default` 키 필수)가 설정되어야 한다.
- `jpa.hibernate.ddl-auto: create`로 되어 있어 앱 기동 시마다 스키마가 재생성된다.
- 실제 발송 성공을 위해서는 고정 IP + SPF 설정이 필요하다(README 참고).

## 아키텍처

### 도메인 모듈 구조

각 도메인(`mailgroup`, `mailinfo`, `sendinfo`, `sendresult`, `sendresultdetail`, `user`)은 동일한 레이어 패턴을 따른다:

- `XxxController` — Thymeleaf 뷰 반환, `ModelMapper`로 `XxxForm` ↔ Entity 변환
- `XxxService` — 트랜잭션 경계, 비즈니스 로직
- `XxxRepository` (Spring Data JPA) + `XxxRepositoryCustom`(인터페이스) + `XxxRepositoryImpl`(QueryDSL 구현, 검색/페이징 담당)
- `XxxForm` — 컨트롤러 바인딩/검증용 DTO
- Entity — `BaseTimeEntity`(생성/수정일시) 상속, Lombok `@Builder`

새 도메인을 추가할 때는 이 5-레이어 패턴을 그대로 따를 것.

### 메일 발송 파이프라인 (`mailprocessor` 패키지)

발송 흐름: `MailProcessorScheduler` → `MailProcessor` → `MacroProcessor`/`MailHeaderEncoder`(헤더/본문 조립) → `SocketMailSender` → `DNSLookup` + `ConnectionManager`

1. **MailProcessorScheduler**: `@Scheduled(cron = "*/10 * * * * *")`로 10초마다 `SendInfo`(상태=`REGISTER`, `sendDate` <= 현재시각)를 조회해 `MailProcessor.process(id)` 호출.
2. **MailProcessor**: 상태를 `SENDING`으로 바꾸고 `SendResult`/`SendResultDetail`(수신자별 발송 결과)을 생성. 수신자 목록을 큐(`LinkedList`)로 순회하며 `ConnectionManager.addConn(domain)`이 허용될 때만 발송하고, 거부되면 큐 뒤로 재삽입(도메인별 동시 연결 수 제한 적용). 전송 후 `SendInfo` 상태를 `COMPLETE`로 변경.
3. **MacroProcessor**: `MailGroup.macroKey`(콤마 구분 키 목록)와 `User.macroValue`(콤마 구분 값 목록)를 매칭해 제목/본문의 `[$key$]` 플레이스홀더를 치환. 콤마 개수는 등록/수정 시 컨트롤러에서 사전 검증됨.
4. **SocketMailSender**: `DNSLookup`으로 수신 도메인의 MX 레코드를 조회해 소켓(port 25)을 열고, HELO → MAIL FROM → RCPT TO → DATA → 본문 → `.` → QUIT 순서로 raw SMTP 명령을 직접 주고받는다. 성공/실패 결과는 `SMTPResult`(코드/메시지)로 반환되어 `SendResultDetail`에 저장.
5. **ConnectionManager / DomainConnectionProperties**: 도메인별 현재 연결 수를 메모리(`HashMap`)로 추적하고, `application.yml`의 `domain.domainConnectionInfo` 설정값(없으면 `default`)을 초과하지 않도록 제어. 발송 완료 시 `removeConn`으로 카운트 반환 필요.

발송 상태(`Status`) 전이: `REGISTER` → (스케줄러가 픽업, `sendDate` 도래 시) → `SENDING` → `COMPLETE`. `WAIT`은 미래 예약 발송을 의미.

### 엔티티 관계

`SendInfo`(발송 건) → `MailInfo`(발송 설정: charset/contentType/encoding/from/replyTo), `MailGroup`(수신 그룹) 참조. `MailGroup`은 여러 `User`(수신자, `MailAddress` + `macroValue`)를 가짐. 발송 시 `SendResult`(1건) ← `SendResultDetail`(수신자별 결과, N건)이 생성됨.

`MailAddress`는 `@Embeddable` 값 객체이며, `StringToMailAddress`/`MailAddressToString` 컨버터(`MailApplicationConfig`에 등록)로 Thymeleaf form의 문자열 입력과 자동 변환된다.

### 아직 미사용/개발 예정

`templete` 패키지(`MailTemplate` 엔티티)와 API 발송 기능은 README에 언급된 예정 기능으로, 아직 서비스/컨트롤러 레이어가 없다.