# money-scatter

## Introduction
- 사용자는 다수의 친구들이 있는 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를 입력하여 뿌리기 요청을 보낼 수 있습니다.
- 요청 시 자신의 잔액이 감소되고 대화방에는 뿌리기 메세지가 발송됩니다.
- 대화방에 있는 다른 사용자들은 위에 발송된 메세지를 클릭하여 금액을 무작위로 받아가게 됩니다.

## 요구 사항
- 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
    - 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
    - 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
    - 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
- 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
- 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 상세 구현 요건 및 제약사항

### 1.뿌리기 API
- 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
- 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
- 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
- token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.

### 2.받기 API
- 뿌리기 시 발급된 token을 요청값으로 받습니다.
- token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
- 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
- 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
- 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
- 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.

### 3.조회 API
- 뿌리기 시 발급된 token을 요청값으로 받습니다.
- token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재상태는 다음의 정보를 포함합니다.
- 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
- 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
- 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.

## API 명세
| Method | API | Parameter | Header | 기능 | 요약 |
|:---:|:---:|:---:|:---:|:---:|:---:| 
| POST | /api/v1/money/throwing | money, personCount | X-USER-ID, X-ROOM-ID| 뿌리기 건 생성 후 고유 token 발급 | 뿌리기 API |
| PATCH | /api/v1/money/throwing | - | X-USER-ID, X-ROOM-ID, X-TOKEN | token에 해당하는 뿌리기 건 중 랜덤 분배 금액 받기 | 받기 API |
| GET | /api/v1/money/throwing | - | X-USER-ID, X-ROOM-ID, X-TOKEN | token에 해당하는 뿌리기 건의 현재 상태 조회 | 조회 API |

### 뿌리기

HTTP Request

```http request
POST /api/v1/money/throwing HTTP/1.1
Content-Type: application/json
X-USER-ID: 111
X-ROOM-ID: room
Host: localhost:8080
{
  "money": 100,
  "personCount": 3
}
```

Request Headers

| Name | Optional | Description |
|:---:|:---:|:---|
| X-USER-ID | true | 사용자 ID |
| X-ROOM-ID | true | 대화방 ID |

Request Fields

| Name | Optional | Description |
|:---:|:---:|:---:|
| money | true | 뿌리는 금액 |
| personCount | true | 받을수 있는 인원 |

HTTP Response

```http request
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Date: Sun, 22 Nov 2020 08:25:18 GMT

Vvi
```

### 받기

HTTP Request

```http request
PATCH /api/v1/money/throwing HTTP/1.1
Content-Type: application/json
X-USER-ID: 222
X-ROOM-ID: room
X-TOKEN: Vvi
Host: localhost:8080
```

Request Headers

| Name | Optional | Description |
|:---:|:---:|:---|
| X-USER-ID | true | 사용자 ID |
| X-ROOM-ID | true | 대화방 ID |
| X-TOKEN | true | 인증토큰 |

HTTP Response

```http request
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Date: Sun, 22 Nov 2020 08:26:18 GMT

66
```

### 조회

HTTP Request

```http request
GET /api/v1/money/throwing HTTP/1.1
Content-Type: application/json
X-USER-ID: 111
X-ROOM-ID: room
X-TOKEN: Vvi
Host: localhost:8080
```

Request Headers

| Name | Optional | Description |
|:---:|:---:|:---|
| X-USER-ID | true | 사용자 ID |
| X-ROOM-ID | true | 대화방 ID |
| X-TOKEN | true | 인증토큰 |

HTTP Response

```http request
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Date: Sun, 22 Nov 2020 08:34:20 GMT

{
	"throwingDate": "2020-11-22T17:25:17.849",
	"throwingMoney": 100.00,
	"receivingMoney": 66.00,
	"receivingMoneyList": [{
		"receivingMoney": 66.00,
		"userId": 222
	}]
}
```


### 에러 응답값
| Message | ErrorCode  | 내용 |
|:---:|:---:|:---:|
| IS_NOT_EXIST_NECESSARY_HEADER | 999 | 필수 헤더값이 없습니다 |
| CAN_RECEIVE_ONLY_OWNER | 998 | 뿌리기를 한사람은 받을 수 없습니다 |
| CAN_RECEIVE_SAME_ROOM | 997 | 해당 방 사용자가 아니면 받을 수 없습니다 |
| IS_NOT_ENOUGH_DISTRIBUTE_MONEY | 996 | 분배할수 있는 돈이 없습니다 |
| IS_EXPIRED_RECEIVE_DATE | 995 | 받을 수 있는 날짜가 만료됐습니다 |
| IS_NOT_EXIST_THROWING_MONEY | 994 | 해당 뿌리기 건이 없습니다 |
| IS_ALREADY_RECEIVE_USER | 993 | 이미 받은 사용자입니다 |
| CAN_SHOW_ONLY_OWNER | 992 | 본인만 조회 가능합니다 |
| IS_EXPIRED_SHOW_DATE | 991 | 조회 만료 일자가 지났습니다 |



## 기능 구현 목록

뿌리기

- [X] 뿌릴 금액, 뿌릴 인원을 요청값로 받는다.
- [X] 뿌리기를 요청한 사람과 방정보를 헤더값으로 받는다.
- [X] 3자리 문자열 token을 발급한다.
- [x] 뿌릴 금액을 인원수에 맞게 분배하여 저장한다.(랜덤 금액)
- [X] 발급한 token을 응답값으로 내려준다


받기

- [x] token과 받을 사람과 방정보를 헤더값으로 받는다.
- [x] 분배되지않은 분배 건 하나를 할당하고 응답값을 내려준다.
- [x] 예외처리
    - [x] 한 사용자당 한번만 받을수 있다.
    - [x] 자기 자신은 받을수 없다.
    - [x] 동일한 대화방에 인원만 받을 수 있다
    - [x] 10분이 지나면 받을 수 없고 실패 응답을 내려준다
 
 
 조회
 
- [x] token과 받을 사람과 방정보를 헤더값으로 받는다.
- [x] 요청 시 현재 상태 응답값을 내려준다(뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
- [x] 예외처리
    - [x] 뿌린 사람만 조회 가능하고 다른사람일 경우 실패 응답을 내려준다
    - [x] 7일동안 조회 가능하고 지나면 실패 응답을 내려준다
    
    
## 문제 해결 전략

 - 뿌리기에 정보를 레디스 키 {token}에 저장하고 TTL 설정으로 만료시간을 체크한다
 - 미리 분배할 돈을 레디스 키 {token}:moneys 에 저장 해놓는다
 - 받기할때 redis에서 분배되어진 돈을 가져오고 받은돈 테이블에 저장한다 
 
 
## 실행 전 로컬 환경 세팅

 - 도커 redis 이미지 불러오기
```
docker pull redis
```

 - redis 컨테이너 생성
```
docker run --name {컨테이너 이름} -d -p 6379:6379 redis
```

 - redis 컨테이너 실행
```
docker start {컨테이너 이름}
```

 - QDomain 생성용 빌드 스크립트 실행
```
gradle task compileQuerydsl
```
