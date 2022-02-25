
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




### HTTP 요청 - 기본, 헤더 조회

#### RequestHeaderController

```java
package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMethod httpMethod,
            Locale locale,
            @RequestHeader MultiValueMap<String, String> headerMap,
            @RequestHeader("host") String host,
            @CookieValue(value = "myCookie", required = false) String cookie
            ){
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("host={}", host);
        log.info("cookie={}", cookie);
        return "ok";
    }
}
```

* HttpServletRequest
* HttpServletResponse
* HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
* Local: Locale 정보를 조회한다.
* @RequestHeader MultiValueMap<String, String> headerMap
  * 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
* @RequestHeader("host") String host
  * 특정 HTTP 헤더를 조회한다.
  * 속성
    * 필수 값 여부 : required
    * 기본 값 속성 : defaultValue
* @CookieValue(value = "myCookie", required = false) String cookie
  * 특정 쿠키를 조회한다.
  * 속성
    * 필수 값 여부 : requried
    * 기본 값 : defaultValue

MultiValueMap

* MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
* HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
  * keyA=value1&keyA=value2

```java
MultiValueMap<String, String> map = new LinkedMultiValueMap();
map.add("keyA", "value1");
map.add("keyA", "value2");

//[value1,value2]
List<String> values = map.get("keyA");
```

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

```java
/**
 * @ResponseBody -> @RestController
 */
@ResponseBody
@RequestMapping("/request-param-v2")
public String requestParamV2(
        @RequestParam("username") String memberName,
        @RequestParam("age") int memberAge
){
    log.info("username = {}, age = {}", memberName, memberAge);
    return "ok";
}
```

* `@RequestParam` : 파라미터 이름으로 바인딩
* `@ResponseBody` : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력

`@RequestParam` 의 name(value) 속성이 파라미터 이름으로 사용

* `@RequestParam("username") String memberName`
* -> `request.getParameter("username")`

```java
@ResponseBody
@RequestMapping("/request-param-v4")
public String requestParamV4(String username, int age){
    log.info("username = {}, age = {}", username, age);
    return "ok";
}
```

`String`, `int`, `Integar` 등의 단순 타입이면 `@RequestParam` 도 생략 가능

> 참고
> 
> 이렇게 애노테이션을 완전히 생략해도 되는데, 너무 없는 것도 약간 과할 수 있다.
> `@RequestParam`이 있으면 명확하게 요청 파라미터에서 데이터를 읽는 다는 것을 알 수 있다.


```java
@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(
        @RequestParam(required = true) String username,
        @RequestParam int age){
    log.info("username = {}, age = {}", username, age);
    return "ok";
}
```

* `@RequestParam.required`
  * 파라미터 필수 여부
  * 기본값이 파라미터 필수(`true`) 이다.

* /request-param 요청
  * username 이 없으므로 400 예외가 발생 
  
> 주의
> 
> 파라미터 이름만 사용
> 
> `/request-param?username=`
> 파라미터 이름만 있고 값이 없는 경우 -> 빈문자로 통과
> 
> 
> 기본형(primitive) 에 null 입력
> 
> `/request-param` 요청
> `@RequestParam(required = false) int age`


```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(
        @RequestParam(required = true, defaultValue = "guest") String username,
        @RequestParam(required = false, defaultValue = "-1") int age){
    log.info("username = {}, age = {}", username, age);
    return "ok";
}
```

파라미터 값이 없는 경우 `defaultValue` 를 사용하면 기본 값을 적용할 수 있다.
이미 기본 값이 있기 때문에 `required`는 의미가 없다.

`defaultValue`는 빈 문자의 경우에도 설정한 기본 값이 적용된다.
`/request-param?username=`

### 파라미터를 Map으로 조회하기 - requestParamMap

```java
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(
        @RequestParam Map<String, Object> paramMap
){
    log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
    return "ok";
}
```

파라미터를 Map, MultiValueMap 으로 조회할 수 있다.

* `@RequestParam Map`
  * `Map<key=value>`
* `@RequestParam MultiValueMap`
  * `MultiValueMap<key=[val1, val2, ...]`


## HTTP 요청 파라미터 - @ModelAttribute

#### 바인딩 받을 객체 - `HelloData`

```java
package hello.springmvc.basic;

import lombok.Data;

@Data
public class HelloData {
    private String username;
    private int age;
}
```

* 롬복 `@Data`
  * `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, `@RequiredArgsConstructor` 를 자동으로 적용해준다.


```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(
        @ModelAttribute HelloData helloData
){
    log.info("username = {}, age = {}",
            helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

HelloData 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.

스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.

* HelloData 객체를 생성한다.
* 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 `setter` 를 호출해서 파라미터의 값을 입력 한다.
* 파라미터의 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다.

#### 프로퍼티

객체에 `getUsername()`, `setUsername()` 메서드가 있으면, 이 객체는 `username` 이라는 프로퍼티를 가지고 있다.
`username` 프로퍼티의 값을 변경하면 `setUsername()` 이 호출되고, 조회하면 `getUsername()` 이 호출된다.

#### 바인딩 오류

`age=abd` 처럼 숫자가 들어가야 할 곳에 문자를 넣으면 `BindException` 이 발생한다.


### ModelAttribute 생략

```java
@ResponseBody
@RequestMapping("/model-attribute-v2")
public String modelAttributeV2(
      HelloData helloData
      ){
      log.info("username = {}, age = {}",
      helloData.getUsername(), helloData.getAge());
      return "ok";
}
```

`@ModelAttribute` 는 생략할 수 있다. 그런데 `@RequestParam` 도 생략할 수 있으니 혼란스러울 수 있다.

스프링은 해당 생략시 다음과 같은 규칙을 적용한다.

* `String`, `int`, `Integer` 같은 단순 타입 = `@RequestParam`
* 나머지 = `@ModelAttribute` (argument resolver 로 지정해준 타입 외)


### HTTP 요청 메세지 - 단순 텍스트

* HTTP message body에 데이터를 직접 담아서 요청 
  * HTTP API에서 주로 사용, JSON, XML, TEXT 
  * 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH

요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 데이터가 넘어오는 경우는 `@RequestParam` , `@ModelAttribute` 를 사용할 수 없다. 

* HTTP 메시지 바디의 데이터를 InputStream 을 사용해서 직접 읽을 수 있다.

```java
@PostMapping("/request-body-string-v1")
public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody={}", messageBody);
    response.getWriter().write("ok");
}
```

#### Input, Output 스트림, Reader - requestBodyStringV2

```java
@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody={}", messageBody);
    responseWriter.write("ok");
}
```

#### 스프링 MVC는 다음 파라미터를 지원한다.

* InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회 
* OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력


```java
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
    String messageBody = httpEntity.getBody();

    log.info("messageBody={}", messageBody);
    return new HttpEntity<>("ok");
}
```

스프링MVC 는 다음 파라미터를 지원한다.

* HttpEntity : HTTP header, body 정보를 편리하게 조회
  * 메시지 바디 정보를 직접 조회
  * 요청 파라미터를 조회하는 기능과 관계 없음 `@RequestParam` X, `@ModelAttribute` X


* `HttpEntity`는 응답에도 사용 가능
  * 메시지 바디 정보 직접 반환
  * 헤더 바디 정보 직접 반환
  * view 조회 x

`HttpEntity` 를 상속받은 다음 객체들도 같은 기능을 제공한다.

* RequestEntity
  * HttpMethod, url 정보가 추가, 요청에서 사용

* ResponseEntity
  * HTTP 상태 코드 설정 가능, 응답에서 사용

> 참고
> 
> 스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP
> 메시지 컨버터( HttpMessageConverter )라는 기능을 사용한다.


```java
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {

    log.info("messageBody={}", messageBody);
    return "ok";
}
```

#### `@RequestBody`

`@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가 필요하다면 
`HttpEntity` 를 사용하거나 `@RequestHeader` 를 사용하면 된다.
이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 `@RequestParam` , `@ModelAttribute` 와는 전혀 관계가 없다.

#### 요청 파라미터 vs HTTP 메시지 바디
* 요청 파라미터를 조회하는 기능: `@RequestParam` , `@ModelAttribute` 
* HTTP 메시지 바디를 직접 조회하는 기능: `@RequestBody`


#### `@ResponseBody`

`@ResponseBody` 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다. 물론 이 경우에도 view를 사용하지 않는다.

```java
@Slf4j
@Controller
public class RequestBodyJsonController {

  private ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/request-body-json-v1")
  public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messageBody={}", messageBody);
    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
  }
}
```

* HttpServletRequest를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.

* 문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환한다.

```java
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

    log.info("messageBody={}", messageBody);
    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

    return "ok";
}
```


* 이전에 학습했던 `@RequestBody` 를 사용해서 HTTP 메시지에서 데이터를 꺼내고 `messageBody`에 저장한다.
* 문자로 된 JSON 데이터인 `messageBody` 를 `objectMapper` 를 통해서 자바 객체로 변환한다.

```java
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData helloData) {

    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

    return "ok";
}
```


* `@RequestBody` 객체 파라미터
  * `@RequestBody HelloData data`
  * `@RequestBody` 에 직접 만든 객체를 지정할 수 있다.

#### `@RequestBody`는 생략 불가능

스프링은 `@ModelAttribute` , `@RequestParam` 해당 생략시 다음과 같은 규칙을 적용한다. 
`String` , `int` , `Integer` 같은 단순 타입 = `@RequestParam`


나머지 = `@ModelAttribute` (argument resolver 로 지정해둔 타입 외)
따라서 이 경우 `HelloData`에 `@RequestBody` 를 생략하면 `@ModelAttribute` 가 적용되어버린다. 

`HelloData data` ->  `@ModelAttribute HelloData data`

따라서 생략하면 HTTP 메시지 바디가 아니라 **요청 파라미터** 를 처리하게 된다.

> 주의
> 
> HTTP 요청시에 content-type이 application/json인지 꼭! 확인해야 한다. 그래야 JSON을 처리할 수
> 있는 HTTP 메시지 컨버터가 실행된다.

* HttpEntity를 사용할 수도 있다.

```java
@ResponseBody
@PostMapping("/request-body-json-v4")
public String requestBodyJsonV4(HttpEntity<HelloData> data) {
  HelloData helloData = data.getBody();
  log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

  return "ok";
}
```
  
```java
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

    return helloData;
}
```

`@ResponseBody`
응답의 경우에도 `@ResponseBody` 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다.
물론 이 경우에도 `HttpEntity` 를 사용해도 된다.

* `@RequestBody` 요청
  * JSON 요청 HTTP 메시지 컨버터 객체

* `@ResponseBody` 응답
  * 객체 HTTP 메시지 컨버터 JSON 응답