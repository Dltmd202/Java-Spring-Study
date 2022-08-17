# 서블릿

* [HttpServletRequest](#HttpServletRequest)
* [HttpServletRequest - 기본 사용법](#HttpServletRequest---기본-사용법)
* [HTTP 요청 데이터](#HTTP 요청 데이터)
* [HTTP 요청 데이터 - GET 쿼리 파라미터](#HTTP-요청-데이터---GET-쿼리-파라미터)
* [HTTP 요청 데이터 - POST HTML Form](#HTTP-요청-데이터---POST-HTML-Form)
* [HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트](#HTTP-요청-데이터---API-메시지-바디---단순-텍스트)
* [HTTP 요청 데이터 - API 메시지 바디 - JSON](#HTTP-요청-데이터---API-메시지-바디---JSON)
* [HttpServletResponse - 기본 사용법](#HttpServletResponse---기본-사용법)
* [HTTP 응답 데이터 - 단순 텍스트, HTML](#HTTP-응답-데이터---단순-텍스트,-HTML)

## Hello 서블릿

스프링 부트 환경에서 서블릿 등록하고 사용

> 참고
>
> 서블릿은 톰캣 같은 웹 어플리케이션 서버를 직접 설치하고, 그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린 다음, 톰캣 서버를
> 실행하면 된다. 하지만 과정은 매우 번거롭다.
> 스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.

### 스프링 부트 서블릿 환경 구성

#### `@ServletComponentScan`
스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 `@ServletComponentScan` 을 지원한다.

#### [ServletApplication](./src/main/java/hello/servlet/ServletApplication.java)

```java
package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan  // 서블릿 자동 등록
@SpringBootApplication
public class ServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServletApplication.class, args);
    }

}
```

### 서블릿 등록하기

#### [HelloServlet](./src/main/java/hello/servlet/basic/HelloServlet.java)

```java
package hello.servlet.basic;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    log.debug("HelloServlet.service");
    log.debug("request = {}", request);
    log.debug("response = {}", response);

    String username = request.getParameter("username");
    log.debug("username = {}", username);

    response.setContentType("text/plain");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write("hello " + username);
  }
}
```

* `@WebServlet` 서브릿 애노테이션
  * name: 서브릿 이름
  * urlPattern: URL 매핑

```java
package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {
    
    String name() default "";
    
    String[] value() default {};
    
    String[] urlPatterns() default {};
    
    int loadOnStartup() default -1;
    
    WebInitParam[] initParams() default {};
    
    boolean asyncSupported() default false;
    
    String smallIcon() default "";
    
    String largeIcon() default "";
    
    String description() default "";
    
    String displayName() default "";
}
```


* HTTP 요청을 통해 매핑된 URL 이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다.
  * `protected void service(HttpServletRequest request, HttpServletResponse response)`



* 웹브라우저 실행

```http request
http://localhost:8080/hello?username=world
```

* 콘솔 실행 결과

```logcatfilter
2022-08-16 18:16:58.494  INFO 63663 --- [nio-8080-exec-2] hello.servlet.basic.HelloServlet         : HelloServlet.service
2022-08-16 18:16:58.499  INFO 63663 --- [nio-8080-exec-2] hello.servlet.basic.HelloServlet         : request = org.apache.catalina.connector.RequestFacade@2fd6a807
2022-08-16 18:16:58.500  INFO 63663 --- [nio-8080-exec-2] hello.servlet.basic.HelloServlet         : response = org.apache.catalina.connector.ResponseFacade@1cb04a39
2022-08-16 18:16:58.502  INFO 63663 --- [nio-8080-exec-2] hello.servlet.basic.HelloServlet         : username = world
```

### HTTP 요청 메시지 로그로 확인하기

서버를 다시 시작하고, 요청해보면 서버가 받은 HTTP 요청 메시지를 출력하는 것을 확인할 수 있다.

```http request
Host: 127.0.0.1:8080
Connection: keep-alive
Cache-Control: max-age=0
sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="96", "Google Chrome";v="96"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: csrftoken=l4KkyRgmn1vadS5ZWHeEbtilXTC8ilGIV8cwDaJRZk1hdyFwMJmAtDKJdIvvJzmV; sessionid=21ia6d6hfxowwgs29itfucvzwlcb9yy5
```

> 참고
>
> 운영서버에 이렇게 모든 요청 정보를 다 남기면 성능저하가 발생할 수 있다. 개발 단계에서만 적용할 것

### 서블릿 컨테이너 동작 방식

#### 내장 톰캣 서버 생성

![](./res/img1.png)

#### HTTP 요청, HTTP 응답 메시지

![](./res/img2.png)

#### 웹 애플리케이션 서버의 요청 응답 구조

![](./res/img3.png)


## HttpServletRequest

#### HttpServletRequest 역할
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다.
서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다.
그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.

#### HTTP 요청 메세지
```http request
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```

* START LINE
  * HTTP 메서드
  * URL
  * 쿼리 스트링
  * 스키마, 프로토콜
* 헤더
  * 헤더 조회
* 바디
  * form 파라미터 형식 조회
  * message body 데이터 직접 조회


#### 임시 저장소 기능
* 해당 HTTP 요청이 시작부터 끝날 때까지 유지되는 임시 저장소 기능
  * 저장: `request.setAttribute(name, value)`
  * 조회: `reqeust.getAttribute(name)`

#### 세션 관리 기능
* `request.getSession(create: true)`


## HttpServletRequest - 기본 사용법

HttpServletRequest가 제공하는 기본 기능들

#### RequestHeaderServlet

```java
package com.example.servlet3.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name="requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
        
        resp.getWriter().write("ok");
    }
}
```

#### start-line 정보

```java
private void printStartLine(HttpServletRequest request) {
    log.info("--- REQUEST-LINE - start ---");
    log.info("request.getMethod() = {}", request.getMethod()); //GET
    log.info("request.getProtocol = {}", request.getProtocol()); //HTTP/1.1
    log.info("request.getScheme() = {}", request.getScheme()); //http
    log.info("request.getRequestURL() = {}", request.getRequestURL()); // http://localhost:8080/request-header
    log.info("request.getRequestURI() = {}", request.getRequestURI()); // /request-test
    log.info("request.getQueryString() = {}", request.getQueryString()); //username=hi
    log.info("request.isSecure() = {}", request.isSecure()); //https 사용 유무
    log.info("--- REQUEST-LINE - end ---");
    }
```

#### 출력결과

```text
2022-08-17 14:28:06.704  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : --- REQUEST-LINE - start ---
2022-08-17 14:28:06.705  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getMethod() = GET
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getProtocol = HTTP/1.1
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getScheme() = http
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getRequestURL() = http://127.0.0.1:8080/request-header
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getRequestURI() = /request-header
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.getQueryString() = null
2022-08-17 14:28:06.707  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : request.isSecure() = false
2022-08-17 14:28:06.708  INFO 38148 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : --- REQUEST-LINE - end ---
```

#### 헤더 정보

```java
//Header 모든 정보
private void printHeaders(HttpServletRequest request) {
    System.out.println("--- Headers - start ---");

//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()){
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + headerName);
//        }

    request.getHeaderNames().asIterator()
                    .forEachRemaining(headerName -> log.info(headerName + ": " + request.getHeader(headerName)));
    log.info("--- Headers - end ---");
}
```

#### 결과

```text
2022-08-17 14:29:51.664  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : --- Headers - start ---
2022-08-17 14:29:51.665  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : host: 127.0.0.1:8080
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : connection: keep-alive
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : cache-control: max-age=0
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-ch-ua: "Chromium";v="104", " Not A;Brand";v="99", "Google Chrome";v="104"
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-ch-ua-mobile: ?0
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-ch-ua-platform: "macOS"
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : upgrade-insecure-requests: 1
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
2022-08-17 14:29:51.667  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-fetch-site: none
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-fetch-mode: navigate
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-fetch-user: ?1
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : sec-fetch-dest: document
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : accept-encoding: gzip, deflate, br
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
2022-08-17 14:29:51.668  INFO 39346 --- [nio-8080-exec-1] c.e.s.b.request.RequestHeaderServlet     : --- Headers - end ---
```

## HTTP 요청 데이터


### 주로 다음 3가지 방법을 사용한다.

* GET - 쿼리 파라미터
  * /url?username=hello&age=20
  * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달 
  * 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
* POST - HTML Form
  * content-type: application/x-www-form-urlencoded
  * 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20 
  * 예) 회원 가입, 상품 주문, HTML Form 사용
* HTTP message body에 데이터를 직접 담아서 요청 
  * HTTP API에서 주로 사용, JSON, XML, TEXT
* 데이터 형식은 주로 JSON 사용 
  * POST, PUT, PATCH


## HTTP 요청 데이터 - GET 쿼리 파라미터


다음 데이터를 클라이언트에서 서버로 전송


전달 데이터
 * username=hello 
 * age=20

메시지 바디 없이, URL의 쿼리 파라미터를 사용해서 데이터를 전달 
예) 검색, 필터, 페이징등에서 많이 사용하는 방식


쿼리 파라미터는 URL에 다음과 같이 `?`를 시작으로 보낼 수 있다. 추가 파라미터는 `&`로 구분한다.


서버에서는 `HttpServletRequest` 가 제공하는 다음 메서드를 통해 쿼리 파라미터를 편리하게 조회할 수 있다.


#### 쿼리 파라미터 조회 메서드

```java
String username = request.getParameter("username"); //단일 파라미터 조회 Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map 으로 조회
String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회
```

#### [RequestParamServlet](./src/main/java/com/example/servlet3/basic/request/RequestParamServlet.java)

```java
package com.example.servlet3.basic.request;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
    request.getParameterNames().asIterator()
            .forEachRemaining(paramName -> System.out.println(paramName +
                    "=" + request.getParameter(paramName)));
    log.info("[전체 파라미터 조회] - end");
    log.info("[단일 파라미터 조회]");
    String username = request.getParameter("username"); System.out.println("request.getParameter(username) = " + username);
    String age = request.getParameter("age");
    log.info("request.getParameter(age) = " + age);
    log.info("[이름이 같은 복수 파라미터 조회]");
    log.info("request.getParameterValues(username)");

    String[] usernames = request.getParameterValues("username");
    for (String name : usernames) {
      log.info("username=" + name);
    }
    resp.getWriter().write("ok");
  }
}
```


#### 복수 파라미터에서 단일 파라미터 조회
* `username=hello&username=kim` 과 같이 파라미터 이름은 하나인데, 값이 중복이면 

`request.getParameter()` 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용해야 한다. 
중복일 때는 `request.getParameterValues()` 를 사용해야 한다.
중복일 때 `request.getParameter()` 를 사용하면 `request.getParameterValues()` 의 첫 번째 값을 반환한다.


## HTTP 요청 데이터 - POST HTML Form

#### 특징
* `content-type: application/x-www-form-urlencoded` 
* 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. `username=hello&age=20`


`application/x-www-form-urlencoded` 형식은 GET에서 살펴본 쿼리 파라미터 형식과 같다. 따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다.
클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만, 서버 입장에서는 둘의 형식이 동일하므로, 
`request.getParameter()` 로 편리하게 구분없이 조회할 수 있다.


결론적으로 `request.getParameter()` 는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML Form 형식도 둘 다 지원한다.


> 참고
> 
> content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.
> GET URL 쿼리 파라미터 형식으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다.
> POST HTML Form 형식으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 
> content-type을 꼭 지정해야 한다. 이렇게 폼으로 데이터를 전송하는 형식을 `application/x-www-form-urlencoded` 라 한다.


## HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트


* HTTP message body에 데이터를 직접 담아서 요청 
  * HTTP API에서 주로 사용, JSON, XML, TEXT 
  * 데이터 형식은 주로 JSON 사용 
  * POST, PUT, PATCH


#### [RequestBodyStringServlet](./src/main/java/com/example/servlet3/basic/request/RequestBodyStringServlet.java)

```java
package com.example.servlet3.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
}
```

> 참고
> inputStream은 byte 코드를 반환한다. byte 코드를 우리가 읽을 수 있는 문자(String)로 보려면 문자표
> (Charset)를 지정해주어야 한다. 여기서는 UTF_8 Charset을 지정해주었다.

## HTTP 요청 데이터 - API 메시지 바디 - JSON


#### JSON 형식 파싱 추가

#### [HelloData](./src/main/java/com/example/servlet3/basic/request/RequestHeaderServlet.java)

```java
package com.example.servlet3.basic;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HelloData {

    private String username;
    private int age;
}
```


#### [ResponseBodyJsonServlet](./src/main/java/com/example/servlet3/basic/request/RequestBodyJsonServlet.java)

```java
package com.example.servlet3.basic.request;

import com.example.servlet3.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletInputStream inputStream = request.getInputStream();
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info(messageBody);
    HelloData hellodData = objectMapper.readValue(messageBody, HelloData.class);

    log.info("hellodData.getUsername() = " + hellodData.getUsername());
    log.info("hellodData.getAge() = " + hellodData.getAge());

    response.getWriter().write("ok");
  }
}
```

> 참고
> 
> JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 
> 라이브러리를 추가해서 사용해야 한다. 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리(`ObjectMapper`)를 
> 함께 제공한다.


## HttpServletResponse - 기본 사용법

#### HTTP 응답 메시지 생성 
* HTTP 응답코드 지정 
* 헤더 생성 
* 바디 생성


#### 편의 기능 제공
* Content-Type, 쿠키, Redirect

#### [ResponseHeaderServlet](./src/main/java/com/example/servlet3/basic/response/ResponseHeaderServlet.java)

```java
package com.example.servlet3.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        [status-line]
        response.setStatus(HttpServletResponse.SC_OK);

//        [response-header]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        content(response);
        cookie(response);
        redirct(response);

        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    private void content(HttpServletResponse response){
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
    }

    private void cookie(HttpServletResponse response){
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);
        response.addCookie(cookie);
    }

    private void redirct(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "/basic/hello-form.html");
    }
}
```

## HTTP 응답 데이터 - 단순 텍스트, HTML


HTTP 응답 메시지는 주로 다음 내용을 담아서 전달한다.


* 단순 텍스트 응답 
  * ( writer.println("ok"); )
* HTML 응답 
* HTTP API - MessageBody JSON 응답

### HttpServletResponse - HTML 응답

#### ResponseHtmlServlet

```java
package com.example.servlet3.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
```

HTTP 응답으로 HTML을 반환할 때는 content-type을 `text/html` 로 지정해야 한다.


## HTTP 응답 데이터 - API JSON

#### ResponseJsonServlet

```java
package com.example.servlet3.basic.response;

import com.example.servlet3.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");

    HelloData helloData = new HelloData();
    helloData.setUsername("kim");
    helloData.setAge(20);

    String result = objectMapper.writeValueAsString(helloData);
    response.getWriter().write(result);
  }
}
```


HTTP 응답으로 JSON을 반환할 때는 content-type을 `application/json` 로 지정해야 한다. 
Jackson 라이브러리가 제공하는 `objectMapper.writeValueAsString()` 를 사용하면 객체를 JSON 문자로 변경할 수 있다.


> 참고
> 
> `application/json` 은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 그래서 스펙에서 charset=utf-8 
> 과 같은 추가 파라미터를 지원하지 않는다. 따라서 `application/json` 이라고만 사용해야지
> `application/json;charset=utf-8` 이라고 전달하는 것은 의미 없는 파라미터를 추가한 것이 된다. 
> `response.getWriter()`를 사용하면 추가 파라미터를 자동으로 추가해버린다. 
> 이때는 `response.getOutputStream()`으로 출력하면 그런 문제가 없다.

