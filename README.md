# SystemMail / 메일 발송 관리 시스템
   
간단한 메일 발송관리 솔루션 입니다.   
sendMail 등 기타 메일 발송 라이브러리 없이 smtp 통신으로 메일을 직접 발송 합니다.   
구동은 spring boot, mysql 환경에서 구동 됩니다.   
회원 정보를 등록 하고 등록한 정보에따라 메일을 발송 하고 결과 확인이 가능 합니다. 발송 결과는 발송 통계와 개별 결과를 확인 가능 합니다.  
회원은 그룹으로 등록이 가능 하고 macro 기능을 적용 가능 합니다. macro 기능은 맞춤형 메일을 보내기 위한 설정 입니다.   
메일 발송 시에는 메일 발송을 위한 설정을 등록 하고 발송이 가능 합니다.   
설정은 charset, encoding, contentType, 보내는 사람, 회신 주소를 입력 하여 설정 합니다.   
그룹과 설정은 최초 등록후 재사용이 가능 합니다.   

현재는 UI 발송만 지원 하고 있고 api 발송 및 관리 기능을 추가할 예정입니다.
## 프로젝트 구성
```
java, spring boot, junit, gradle, jpa, querydsl, thymeleaf, mysql

추가적으로 일부 UI 편의성을 위해 javacript, jquery 가 사용 되었습니다.
```
## 초기 설정

설치 시 사전 설정이 필요 합니다.   
메일 발송 요건을 설정할 필요가 있습니다.  
application.yml 의 설정을 추가해야 합니다.   

```
mail:      
  domain: sender.com                  //발송 도메인 서버 도메인이 아닌 메일을 발송 할떄 사용 하는 도메인 입니다.   
  propertyMap:    
    returnPath: test@test.co.kr       // returnPath 입니다 smtp 통신 단계의 회신 주소 입니다.   
    MIME-Version: 1.0                 // MIME-Version           
    DKIM-Signature: test              // dkim 을 사용 할경우 설정이 가능합니다.    
``` 
    
위 설정은 기본적인 메일 발송 관련 설정 이고    
실제 발송 성공을 처리 하려면 서버는 고정 ip 로 ip가 나가야 하고 spf 설정이 필요 합니다.    
[https://spam.kisa.or.kr](https://spam.kisa.or.kr/white/sub2_R.do?idx=8&currentPage=1&category=&field=&keyword=)
페이지에 spf 관련 정보가 있습니다.    




### DB 구성 
```
예시
```

### 흐름도

```
예시
```

## Running the tests 
어떻게 테스트가 이 시스템에서 돌아가는지에 대한 설명을 합니다

### 테스트는 이런 식으로 동작합니다
```
예시
```

### 테스트는 이런 식으로 작성하시면 됩니다

```
예시
```
