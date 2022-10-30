# 스프링 MVC - 기본 기능

## 프로젝트 구성

#### build.gradle

```java
plugins {
    id 'org.springframework.boot' version '2.6.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

## 요청 매핑

#### [MappingController](./src/main/java/hello/springmvc/basic/requestmapping/MappingController.java)

```java
package hello.springmvc.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }
}
```

### 매핑 정보

* `@RestController`
  * `@Controller`는 반환 값이 `String`이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 렌더링 된다.
  * `@RestController`는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.
    따라서 실행 결과로 ok 메시지를 받을 수 있다. `@ResponseBody`와 관련이 있다.
* `@RequestMapping("/hello-basic")`
  * `/hello-basic` URL 호출이 오면 이 메서드가 실행되도록 매핑한다.
  * 대부분의 속성을 `배열[]`로 제공하므로 다중 설정이 가능하다. `{"/hello-basic", "/hello-go"}`


### HTTP 메서드 

`@RequestMapping`에 `method` 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출된다.

모두 혀용 GET, HEAD, POST, PUT, PATCH, DELETE

### HTTP 메서드 매핑

```java
/**
 * method 특정 HTTP 메서드 요청만 허용
 * GET, HEAD, POST, PUT, PATCH, DELETE
 */
@RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
public String mappingGetV1(){
    log.info("mappingGetV1");
    return "ok";
}
```

만약 여기에 POST 요청을 하면 스프링 MVC는 HTTP 405 상태코드(Method Not Allowed)를 반환한다.

#### HTTP 메서드 매핑 축약

```java
/**
 * 편리한 축약 애노테이션
 * @GetMapping
 * @PostMapping
 * @PutMapping
 * @DeleteMapping
 * @PatchMapping
 */
@GetMapping("/mapping-get-v2")
public String mappingGetV2(){
    log.info("mapping-get-v2");
    return "ok";
}
```


HTTP 메서드를 축약한 애노테이션을 사용하는 것이 더 직관적이다. 코드를 보면 내부에서 
`@RequestMapping` 과 `method` 를 지정해서 사용하는 것을 확인할 수 있다.

#### PathVariable 사용 

```java
/**
 * PathVariable 사용
 * 변수명이 같으면 생략 가능
 *
 * @PathVariable("userId") String userId -> @PathVariable userId
 * /mapping/userA
 */
@GetMapping("/mapping/{userId}")
public String mappingPath(@PathVariable("userId") String data){
    log.info("mappingPath userId = {}", data);
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
@GetMapping(value = "/mapping-param", params = "mode=debug")
public String mappingParam(){
    log.info("mappingParam");
    return "ok";
}
```

#### 실행

* http://localshot:8080/mapping-param?mode=debug

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

### 미디어 타입 조건 매핑 - HTTP 요청 Content-Type, consume

```java
/**
 * Content-Type 헤더 기반 추가 매핑 Media Type * consumes="application/json"
 * consumes="!application/json"
 * consumes="application/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE
 */
@PostMapping(value = "/mapping-consume", consumes = "application/json")
public String mappingConsumes(){
    log.info("mappingConsumes");
    return "ok";
}
```

* HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
* 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환한다.

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
만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환한다.

## 요청 매핑 - API

### 회원 관리 API

|     분류     |   메서드    | API                |
|:----------:|:--------:|:-------------------|
|  회원 목록 조회  |   GET    | `/users`           |
|   회원 등록    |   POST   | `/users`           |
|   회원 조회    |   GET    | `/users/{userId}`  |
|   회원 수정    |  PATCH   | `/users/{userId}`  |
|   회원 삭제    |  DELETE  | `/users/{userId}`  |

#### [MappingClassController](./src/main/java/hello/springmvc/basic/requestmapping/MappingClassController.java)

```java
package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {

    @GetMapping
    public String user(){
        return "get users";
    }

    @PostMapping
    public String addUser(){
        return "post user";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId){
        return "get userId =" + userId;
    }

    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId){
        return "update userId = " + userId;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId){
        return "delete userId = " + userId;
    }

}
```

* `@RequestMapping("/mapping/users")`
  * 클래스 레벨에 매핑 정보를 두면 메서드 레벨에서 해당 정보를 조합해서 사용한다.

## HTTP 요청 - 기본, 헤더 조회

#### [RequestHeaderController](./src/main/java/hello/springmvc/basic/request/RequestHeaderController.java)

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

* `HttpServletRequest`
* `HttpServletResponse`
* `HttpMethod`: HTTP 메서드를 조회한다. - `org.springframework.http.HttpMethod`
* `Locale`: Locale 정보를 조회한다.
* `@RequestHeader MultiValueMap<String, String> headerMap`
  * 모든 HTTP 헤더를 MultiValueMap 형식으로 조회
* `@RequestHeader("host") String host`
  * 특정 HTTP 헤더를 조회한다.
  * 속성
    * 필수 값 여부: `required`
    * 기본 값 속성: defaultValue
* `@CookieValue(value = "myCookie", required = false) String cookie`
  * 특정 쿠키를 조회한다.
  * 속성
    * 필수 값 여부: required
    * 기본값: defaultValue

#### `MultiValueMap`

* Map과 유사한 형태로, 하나의 키에 여러 값을 받을 수 있다.
* Http header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
  * `KetA=value1&keyA=value2`

```java
MultiValueMap<String, String> map = new LinkedMultiValueMap();
map.add("keyA", "value1");
map.add("keyA", "value2");

List<String> values = map.get("keyA");
```

## HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

### HTTP 요청 데이터 조회 - 개요

#### 클라이언트에서 서버로 데이터를 전달할 때는 주로 3가지 방법을 사용한다.

* GET - 쿼리 파라미터
  * /url?username=hello&age=20
  * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함하는 전달
* POST - HTML Form
  * content-type:application/x-www-form-urlencoded
  * 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello?age=20
* HTTP message body에 데이터를 직접 담이서 요청
  * HTTP API에서 주로 사용, JSON, XML, TEXT
  * 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH


#### [RequestParamController](./src/main/java/hello/springmvc/basic/request/RequestParamController.java)

```java
package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

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

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age
    ){
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age){
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age){
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age){
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            @RequestParam Map<String, Object> paramMap
    ){
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(
            @ModelAttribute HelloData helloData
    ){
        log.info("username = {}, age = {}",
                helloData.getUsername(), helloData.getAge());
        return "ok";
    }
    
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(
            HelloData helloData
    ){
        log.info("username = {}, age = {}",
                helloData.getUsername(), helloData.getAge());
        return "ok";
    }
}
```

## HTTP 요청 파라미터 - @ModelAttribute

#### [HelloData](./src/main/java/hello/springmvc/basic/HelloData.java)

```java
package hello.springmvc.basic;

import lombok.Data;

@Data
public class HelloData {
    private String username;
    private int age;
}
```

#### @ModelAttribute 적용 - modelAttributeV1

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

* 스프링 MVC는 `@ModelAttribute`가 있으면 다음을 실행시킨다.

  * HelloData 객체를 생성한다.
  * 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서
    파라미터를 바인딩한다.

#### 프로퍼티

* 객체에 `getUsername()`, `setUsername()` 메서드가 있으면, 이 객체는 `username`이라는 프로퍼티를 가지고 있는 것이다.
* `username` 프로퍼티의 값을 변경하면 `setUsername()`이 호출되고, 조회하면 `getUsername()`이 호출된다.

#### @ModelAttribute 생략 - modelAttributeV2

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

* `@ModelAttribute`는 생략할 수 있다.

## HTTP 요청 메시지 - 단순 텍스트

* HTTP message body에 데이터를 직접 담아서 요청
  * HTTP API에서 주로 사용, JSON, XML, TEXT
  * 데이터 형식은 주로 JSON 사용
  * POST, PATCH, PUT

* 요청 파라미터와 다르게 HTTP 메시지 바디를 통해 데이터가 직접 데이터가 넘어오는 경우는
  `@RequestParam`, `@ModelAttribute`를 사용할 수 없다.

  * HTTP 메시지의 바디의 데이터를 `InputStream`을 사용하여 직접 읽을 수 있다.

#### [RequestBodyStringController](./src/main/java/hello/springmvc/basic/request/RequestBodyStringController.java)

```java
package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();

        log.info("messageBody={}", messageBody);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        return "ok";
    }
}
```

#### 스프링 MVC는 다음 파라미터를 지원한다.

* InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회 
* OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

#### HttpEntity - requestBodyStringV2

```java
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
    String messageBody = httpEntity.getBody();

    log.info("messageBody={}", messageBody);
    return new ResponseEntity<>("ok", HttpStatus.OK);
}
```

#### 스프링 MVC는 다음 파라미터를 지원한다.

* HttpEntity: HTTP header, body 정보를 편리하게 조회
  * 메시지 바디 정보를 직접 조회
  * 요청 파라미터를 조회하는 기능과 관계 없음
* HttpEntity는 응답에도 사용 가능
  * 메시지 바디 정보 직접 반환
  * 헤더 정보 포함 가능
  * view 조회 X

`HttpEntity`를 상속받은 다음 객체들도 같은 기능을 제공한다.

* RequestEntity
  * HttpMethod, url 정보가 추가, 요청에서 사용
* ResponseEntity
  * HTTP 상태 코드 설정 가능, 응답에서 사용
  * `return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);`

#### @RequestBody

`@RequestBody`를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.
헤더 정보가 필요하다면 `HttpEntity`를 사용하거나 `@RequestHeader`를 사용하면 된다.
이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 `@RequestParam`, `@ModelAttribute`
와는 전혀 관계가 없다.


#### 요청 파라미터와 HTTP 메시지 바디

* 요청 파라미터를 조회하는 기능: `@RequestParam`, `@ModelAttribute`
* HTTP 메시지 바디를 직접 조회하는 기능: `@RequestBody`

#### @ResponseBody

`@ResponseBody`를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.

## HTTP 요청 메시지 - JSON

JSON 데이터 형식을 이용한 조회


#### [RequestBodyJsonController](./src/main/java/hello/springmvc/basic/request/RequestBodyJsonController.java)

```java
package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> data) {
        HelloData helloData = data.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return helloData;
    }
}
```

* `HttpServletRequest`를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
* 문자로 된 JSON 데이터를 Jackson 라이브러리인 `objectMapper`를 사용해서 자바 객체로 반환한다.

#### @RequestBody 문자 변환

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