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

