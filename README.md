# 항해99 백엔드 플러스 3주차 과제 (CH 2-1. 서버 구축)

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```

# e-커머스 서비스

## 시나리오 요구사항 분석

### 잔액 충전 API

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 충전 요청 (userId, 충전금액)
    A->>DB: 사용자 잔액 조회 (userId)
    DB-->>A: 사용자 잔액 반환
    A->>DB: (잔액 + 충전금액)으로 업데이트
    DB-->>A: 업데이트 결과
    A-->>U: 충전 완료 응답
```

### Request
```http request
POST /api/v1/users/{userId}/balance
Content-Type: application/json

{
    "amount": 10000
}
```

### Response
```http request
204 No Content
Content-Type: application/json
Location: /api/v1/users/{userId}/balance
```

### Error Response

#### 요청 값이 잘못된 경우 (400 Bad Request)

- 음수 값으로 충전 요청
- 한번에 충전할 수 있는 최대 금액 초과

```http request
400 Bad Request
Content-Type: application/json

{
    "errorCode": "INVALID_AMOUNT_REQUEST",
    "message": "충전 요청 금액이 올바르지 않습니다."
}
```

#### 사용자가 존재하지 않는 경우 (404 Not Found)

- 존재하지 않는 사용자 ID로 요청

```http request
404 Not Found
Content-Type: application/json

{
    "errorCode": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
}
```

### 잔액 조회

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 잔액 조회 요청 (userId)
    A->>DB: 잔액 조회 (userId)
    DB-->>A: 사용자 잔액 반환
    A-->>U: 잔액 정보 응답
```

### Request
```http request
GET /api/v1/users/{userId}/balance
Content-Type: application/json
```

### Response
```http request
200 OK
Content-Type: application/json

{
    "balance": 10000
}
```

### Error Response
```http request
404 Not Found
Content-Type: application/json

{
    "errorCode": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
}
```

### 상품 조회

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 상품 리스트 조회 요청
    A->>DB: 상품 정보(ID, 이름, 가격, 재고수량) 조회
    DB-->>A: 상품 리스트 반환
    A-->>U: 상품 리스트 응답
```

### Request
```http request
GET /api/v1/products?page=0&size=10&sort=price,desc
Content-Type: application/json
```

#### Query Parameters

| 파라미터명  | 타입     | 설명                            | 필수 여부 | 기본값            |
|--------|--------|-------------------------------|-------|----------------|
| `page` | int    | 페이지 번호 (ex. `page=0`)         | N     | 0              |
| `size` | int    | 페이지 당 아이템 수  (ex. `size=20`)  | N     | 10             |
| `sort` | string | 정렬 기준 (ex. `sort=price,desc`) | N     | createdAt,desc |

### Response
```http request
200 OK
Content-Type: application/json

{
    "page": 1,
    "size": 10,
    "totalElements": 57,
    "totalPages": 6,
    "sort": "price,desc",
    "content": [
        {
            "productId": "P003",
            "productName": "Premium Item",
            "price": 30000,
            "quantity": 20,
            "createdAt": "2021-08-01T00:00:00",
        },
        {
            "productId": "P002",
            "productName": "Great Item",
            "price": 20000,
            "quantity": 3,
            "createdAt": "2021-08-01T00:00:00",
        },
        ...
    ]
}
```

### 선착순 할인 쿠폰 발급

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 선착순 쿠폰 발급 요청(userId)
    A->>DB: 발급 가능 쿠폰 수량 체크
    DB-->>A: 남은 쿠폰 개수 반환
    alt 쿠폰 수량 > 0
        A->>DB: 쿠폰 발급 & 사용자에게 할당
        DB-->>A: 성공 응답(쿠폰 정보)
        A-->>U: 쿠폰 발급 성공 응답
    else 쿠폰 수량 == 0
        A-->>U: 쿠폰 발급 실패(매진)
    end
```

### Request
```http request
POST /api/v1/coupons/{couponId}
Content-Type: application/json

{
    "userId": "user1"
}
```

### Request Parameters

| 파라미터명    | 타입  | 설명              | 필수 여부 | 기본값 |
|----------|-----|-----------------|-------|-----|
| `userId` | int | 쿠폰을 발급받을 사용자 ID | Y     |     |

### Response
```http request
201 Created
Content-Type: application/json
Location: /api/v1/users/{userId}/coupons
```

### Error Responses

#### 쿠폰 발급 실패 (409 Conflict)

- 남은 쿠폰 수량이 0인 경우

```http request
409 Conflict
Content-Type: application/json

{
    "errorCode": "COUPON_SOLD_OUT",
    "message": "쿠폰이 모두 소진되었습니다."
}
```

#### 존재하지 않는 쿠폰을 요청 (404 Not Found)

- 존재하지 않는 쿠폰 ID로 요청

```http request
404 Not Found
Content-Type: application/json

{
    "errorCode": "COUPON_NOT_FOUND",
    "message": "존재하지 않는 쿠폰입니다."
}
```

### 유저가 보유한 쿠폰 조회

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 보유 쿠폰 목록 조회 요청 (userId)
    A->>DB: 사용자 쿠폰 목록 조회
    DB-->>A: 쿠폰 목록 반환
    A-->>U: 쿠폰 목록 응답
```

### Request
```http request 
GET /api/v1/users/{userId}/coupons
Content-Type: application/json
```

### Response
```http request
200 OK
Content-Type: application/json

[
    {
        "couponId": "C001",
        "createdAt": "2021-08-01T00:00:00",
    },
    {
        "couponId": "C002",
        "createdAt": "2021-08-01T00:00:00",
    },
    ...
]
```

### 상품 주문

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB
    participant D as Data Platform(외부)

    Note over U,A: 주문 요청: (userId, (상품 ID, 수량), couponId 등)

    U->>A: 주문/결제 요청 (userId, items[], couponId?)
    A->>DB: 사용자 잔액 조회 (userId)
    DB-->>A: 사용자 잔액 반환

    alt 쿠폰 사용 시
        A->>DB: 쿠폰 유효성 검사 (couponId, userId)
        DB-->>A: 쿠폰 유효 여부 반환
        A->>A: 할인 금액 계산
    end

    A->>DB: 상품 재고 확인(각 상품 ID, 주문수량)
    DB-->>A: 재고 상태 반환

    alt 재고 충분
        A->>A: 결제 금액 계산(상품금액 - 할인)
        A->>DB: 잔액 차감
        DB-->>A: 차감 결과 (성공/실패)

        alt 잔액 충분(결제 성공)
            A->>DB: 재고 차감
            DB-->>A: 재고 차감 성공
            A->>DB: 쿠폰 사용 처리(만약 쿠폰 사용했다면)
            DB-->>A: 쿠폰 사용 처리 완료

            par 주문 응답 흐름
                A-->>U: 주문/결제 성공 응답
            and 데이터 플랫폼 전송 흐름
                A->>D: 결제 성공 정보 전송
                alt 전송 성공
                    D-->>A: 수신 성공(200 OK 등)
                else 전송 실패
                    A->>A: **재시도 로직(백그라운드 등)**
                    Note right of A: <br>1) 실패 원인 로깅<br>2) 일정 interval마다 재전송<br>3) MQ, 백엔드 Job 등 활용
                end
            end
        else 잔액 부족(결제 실패)
            A-->>U: 결제 실패 응답
        end
    else 재고 부족
        A-->>U: 주문 실패 응답(재고 부족)
    end
```

### Request
```http request
POST /api/v1/orders
Content-Type: application/json

{
    "userId": "user1",
    "couponId": "C001",
    "products": [
        {
            "productId": "P001",
            "quantity": 2
        },
        {
            "productId": "P002",
            "quantity": 1
        }
    ]
}
```

### Response
```http request
201 Created
Content-Type: application/json
Location: /api/v1/orders/{orderId}

{
    "orderId": "O001",
    "totalPrice": 50000,
    "orderedAt": "2021-08-01T00:00:00",
}
```

### Error Responses

#### 잔액 부족 (402 Payment Required)

- 주문 금액이 잔액보다 큰 경우

```http request
402 Payment Required
Content-Type: application/json

{
    "errorCode": "INSUFFICIENT_BALANCE",
    "message": "잔액이 부족합니다."
}
```

#### 재고 부족 (409 Conflict)

- 주문 수량이 재고 수량보다 많은 경우

```http request
409 Conflict
Content-Type: application/json

{
    "errorCode": "INSUFFICIENT_STOCK",
    "message": "재고가 부족합니다."
}
```

#### 쿠폰 유효하지 않음 (400 Bad Request)

- 유효하지 않은 쿠폰으로 요청한 경우 (쿠폰이 존재하지 않거나, 사용자에게 할당되지 않은 경우)

```http request
400 Bad Request
Content-Type: application/json

{
    "errorCode": "INVALID_COUPON",
    "message": "유효하지 않은 쿠폰입니다."
}
```

#### 상위 상품 조회

```mermaid
sequenceDiagram
    participant U as User
    participant A as E-commerce App
    participant DB as DB

    U->>A: 상위 상품 조회 요청(최근 N일)
    A->>DB: 최근 N일간 판매 데이터 조회 (Top 5)
    DB-->>A: Top 5 상품 통계 반환
    A->>A: (필요 시) 추가 상품 정보 매핑(이름, 가격 등)
    A-->>U: Top 5 상품 정보 응답
```

### Request
```http request
GET /api/v1/products/top-sold?startDate=2021-08-01T00:00:00Z&endDate=2021-08-07T00:00:00Z&page=0&size=5
Content-Type: application/json
```

### Request Parameters

| 파라미터명             | 타입   | 설명                          | 필수 여부 | 기본값 |
|-------------------|------|-----------------------------|-------|-----|
| `startDate`       | date | 조회 시작 날짜 (UTC)              | Y     |     |
| `endDate`         | date | 조회 종료 날짜 (UTC)              | Y     |     |
| `page`            | int  | 페이지 번호 (ex. `page=2`)       | N     | 0   |
| `size`            | int  | 페이지 당 아이템 수 (ex. `size=20`) | N     | 5   |

### Response
```http request
200 OK
Content-Type: application/json

[
    {
        "productId": "P001",
        "productName": "Best Item",
        "price": 10000,
        "quantity": 100,
        "totalSold": 50,
    },
    {
        "productId": "P002",
        "productName": "Great Item",
        "price": 20000,
        "quantity": 30,
        "totalSold": 30,
    },
    ...
]
```

### Error Response

#### 조회 기간이 너무 큰 경우 (400 Bad Request)

- 조회 기간이 180일을 초과하는 경우

```http request
400 Bad Request
Content-Type: application/json

{
    "errorCode": "INVALID_PERIOD",
    "message": "조회 기간은 최대 180일까지 가능합니다."
}
```

#### 조회 상품 수가 너무 큰 경우 (400 Bad Request)

- 조회 상품 수가 100개를 초과하는 경우

```http request
400 Bad Request
Content-Type: application/json

{
    "errorCode": "INVALID_LIMIT",
    "message": "조회 상품 수는 최대 100개까지 가능합니다."
}
```

## ERD

### 도메인 모델

```mermaid

classDiagram
    class User {
        <<Entity>>
        - id: String
        - name: String
        - balance: Money
    }
    
    class Money {
        <<ValueObject>>
        - amount: Long
        + add(amount: Long): Money
        + subtract(amount: Long): Money
    }

    class Product {
        <<Entity>>
        - id: String
        - name: String
        - price: Long
        - quantity: Integer
    }

    class Coupon {
        <<Entity>>
        - id: String
        - userId: String
    }

    class Order {
        <<Entity>>
        - id: String
        - userId: String
        - totalPrice: Long
        - orderedAt: LocalDateTime
        + placeOrder(userId: String, items: List<OrderItem>, couponId: String): Order
    }

    class OrderItem {
        <<Entity>>
        - id: String
        - orderId: String
        - productId: String
        - quantity: Integer
    }

    class ProductStat {
        <<ValueObject>>
        - productId: String
        - totalSold: Integer
    }

    User "1" -- "N" Coupon : has
    User "1" -- "N" Order : has
    User "1" -- "1" Money : has
    Product "1" -- "N" OrderItem : has
    Order "1" -- "N" OrderItem : has
    Product "1" -- "N" ProductStat : has

```