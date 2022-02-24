
## 로깅 간단히 알아보기

운영 시스템에서는 `System.out.println()` 같은 시스템 콘솔을 사용해서 필요한 정보를 출력하지 않고, 별도의
로깅 라이브러리를 사용해서 로그를 출력한다.

### 로깅 라이브러리

스프링 부트 라이브러리를 사용하면 스프링 부트 로깅 라이브러리 `spring-boot-starter-logging` 가 함께
포함된다. 스프링 부트 로깅 라이브러리는 기본으로 다음로깅 라이브러리를 사용한다.

* SLF4J
* Logback

로그 라이브러리는 Logback. Log4J, Log4J2 등등 수 많은 라이브러리가 있는데, 그것을 통합해서 인터페이스로
제공하는 것이 SLF4J 라이브러리이다.
SLF4J는 인터페이스이고, 그 구현체로 Logback 가은 로그 라이브러리를 선택하게 된다.

### 로그 선언

* `private Logger log = LoggerFactory.getLogger(getClass());`
* `private static final Logger log = LoggerFactory.getLogger(Xxx.class);`
* `@Slf4j`: 롬복 사용 가능

### 매핑 정보

* `@RestController`
  * `@Controller` 는 반환 값이 String  이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 렌더링 된다.
  * `@RestController` 는 반환 값으로 뷰를 찾는 것이 아니라 HTTP 메시지 바디에 바로 입력 한다.


### 테스트

* 로그가 출력되는 포멧 확인
    * 시간, 로그 레벨, 프로세스 ID, 쓰레드 이름, 클래스 이름, 로그 메시지
* 로그 레벨 설정을 변경해서 출력 결과를 보자
  * LEVEL: `TRACE` > `DEBUG` > `INFO` > `WARN` > `ERROR`
  * 개발 서버는 debug 출력
  * 운영 서버는 info 출력

### 로그 레벨 설정
`application.properti es`
```text
# 전체 로그 레벨 설정(기본 info)
logging.level.root=debug
```

### 올바른 로그 사용법

* `log.debug("data =" + data)`
  * 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 `"data = " + data` 가 실제 실행이 되어 버린다. 
    결과적으로 문자 더하기 연산이 발생한다.
* `log.debug("data={}", data)`
  * 로그 출력 레벨을 info로 설정하면 아무일도 발생하지 않는다. 따라서 앞과 같은 의미없는 연산이 발생하지 않는다.

### 로그 사용시 장점

* 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
* 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게
  조절할 수 있다.
* 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로
  남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다
* 성능도 일반 System.ou보다 좋다.(내부 버퍼링, 멀티 쓰레드 등등) 

## 요청 매핑

### 매핑 정보

* `@RestController`
  * `@Controller` 는 반환 값이 String 이면
  뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
  * `@RestController` 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.
    따라서 실행 결과로 메세지를 받을 수 있다.

* `@RequestMapping("/hello-basic")`
  * `/hello-basic` URL 이 호출이 되면 이 메서드가 실행되도록 매핑한다.
  * 대부분의 속성을 `배열[]` 로 제공하므로 다중 설정이 가능하다. `{"/hello-basic", "/hello-go"}`
  
* 둘 다 허용
  * 매핑 : `/hello-basic`
  * URL 요청 : `/hello-basic`, `/hello-basic/`

* HTTP 메서드

@RequestMapping 에 method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출된다.

모두 허용 `GET`, `HEAD`, `POST`, `PUT`, `PATCH`, `DELETE`

### PathVariable(경로 변수) 사용

최근 HTTP API는 다음과 같이 리소스 경로에 식별자를 넣는 스타일을 선호

* `/mapping/userA`
* `/users/1`

* `@RequestMapping` 은 URL 경로를 템플릿화 할 수 있는데,
  `@PathVariable` 을 사용하면 매칭 되는 부분을 편리하게 조회할 수 있다.

* `@PathVariable` 의 이름과 파라미터 이름이 같으면 생략할 수 있다.

### PathVariable 사용 - 다중 

```java
/**
 * PathVarlue 다중 사용
 * @param userId
 * @param orderId
 * @return
 */
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId){
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
    return "ok";
}
```

### 특정 파라미터 조건 매핑

```java
/**
 * 파라미터 추가 매핑
 * 해당 파라미터가 없으면 매핑 되지 않음
 * params="mode",
 * params="!mode",
 * params="mode=debug",
 * params="mode=!debug",
 * params={"mode=debug", "data=good"}
 */
@GetMapping(value = "/mapping-param", params = "mode-debug")
public String mappingParam(){
    log.info("mappingParam");
    return "ok";
}
```

특정 파라미터가 있거나 없는 조건을 추갛할 수 있다.

### 특정 헤더 조건 매핑

```java
/**
 * 특정 헤더로 추가 매핑
 * headers="mode",
 * headers="!mode",
 * headers="mode=debug",
 * headers="mode=!debug",
 */
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader(){
    log.info("mappingParam");
    return "ok";
}
```

파라미터 매핑과 비슷하지만, HTTP 헤더를 사용한다.


### 미디어 타입 조건 매핑 - HTTP 요청 Content-Type, consume

```java
/**
 * Content-Type 헤더 기반 추가 매핑 Media Type
 * consumes="application/json"
 * consumes="!application/json"
 * consumes="application/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE
 * @return
 */
@PostMapping(value = "/mapping-consume", consumes = "application/json")
public String mappingConsumes(){
    log.info("mappingConsumes");
    return "ok";
}
```

HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
만약 맞지 않으면 415 상태코드를 반환한다.

### 미디어 타입 조건 매핑 - HTTP 요청 Accept, produce

```java
/**
 * Accept 헤더 기반 Media Type
 * produces="text/html"
 * consumes="!text/html"
 * consumes="text/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE
 * @return
 */
@PostMapping(value = "/mapping-consume", produces = "text/html")
public String mappingProduces(){
    log.info("mappingConsumes");
    return "ok";
}
```

HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑한다.
만약 맞지 않으면 HTTP 406 상태코드을 반환한다.

## 요청 매핑 - API 예시

### 회원 목록 API

* 회원 목록 조회 : GET `/users`
* 회원 등록 : POST `/users`
* 회원 조회 : GET `/users/{userId}`
* 회원 수정 : PATCH `/users/{userId}`
* 회원 삭제 : DELTE `/users/{userId}`


## HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

### HTTP 요청 데이터 조회 - 개요

> 클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다.

* GET - 쿼리 파라미터
  * `/url?username=hello&age=20`
  * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  * 예) 검색, 필터, 페이징등에서 많이 사용하는 방식

* POST - HTML Form
  * `content-type: application/x-www-form-urlencoded`
  * 메시지 바디에 쿼리 파라미터 형식으로 전달 `username=hello&age=20`
  * 예) 회원 가입, 상품 주문, HTML Form 사용

* HTTP message body 에 데이터를 직접 담아서 요청
  * HTTP API에서 수로 사용, JSON, XML, TEXT
  * 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH


### 요청 파라미터 - 쿼리 파라미터, HTML Form

`HttpServletRequest` 의 `request.getParameter()` 를 사용하면 다음 두 가지 요청 파라미터를 조회할 수 있다.

### GET, 쿼리 파라미터 전송


