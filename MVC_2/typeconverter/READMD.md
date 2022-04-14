# 스프링 타입 컨버터

### 스프링 타입 컨버터

문자를 숫자로 변환하거나, 반대로 숫자를 문자로 변환해야 하는 것 처럼 애플리케이션을 개발하다 보면 타입을 변환해야 하는 경우가 상당히 많다.

#### HelloController - 문자 타입을 숫자 타입으로 변경

```java
package hello.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request){
        String data = request.getParameter("data");
        Integer integer = Integer.valueOf(data);
        System.out.println("integer = " + integer);
        return "ok";
    }
}
```

분석

`String data = request.getParameter("data")`

HTTP 요청 파라미터는 모두 문자로 처리된다. 따라서 요청 파라미터를 자바에서 다른 타입으로 변환해서 사용하고 싶으면 다음과 같이 
숫자 타입으로 변환하는 과정을 거쳐야 한다.

`Integer intValue = Integer.valueOf(data)`

스프링 MVC가 제공하는 `@RequestParam` 을 사용

#### HelloController

```java
@GetMapping("/hello-v2")
public String helloV2(@RequestParam Integer data){
    System.out.println("data = " + data);
    return "ok";
}
```

앞서 보았듯이 HTTP 쿼리 스트링으로 전달하는 `data=10` 부분에서 10은 숫자 10이 아니라 문자 10이다. 
스프링이 제공하는 `@RequestParam` 을 사용하면 이 문자 10을 `Integer` 타입의 숫자 10으로 편리하게 받을 수 있다.

이것은 스프링이 중간에서 타입을 변환해주었기 때문이다.

#### @ModelAttribute 타입 변환 예시

```java
@ModelAttribute UserData data

class UserData {
    Integer data;   
}
```

#### @PathVariable 타입 변환 예시

```java
/users/{userId}

@PathVariable("data") Integer data
```

#### 스프링과 타입 변환

이렇게 타입을 변환해야 하는 경우는 상당히 많다.
스프링이 중간에 타입 변환기를 사용해서 타입을 `String` -> `Integer` 로 변환해주었기 때문에 개발자는 편리하게 해당 타입을 바로 받을 수 있다.
앞에서는 문자를 숫자로 변경하는 예시를 들었지만, 반대로 숫자를 문자로 변경하는 것도 가능하고, `Boolean` 타입을 숫자로 변경하는 것도 가능하다.

#### 컨버터 인터페이스

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {
    T convert(S source);
}
```

스프링은 확장 가능한 컨버터 인터페이스를 제공한다.
개발자는 스프링에 추가적인 타입 변환이 필요하면 이 컨버터 인터페이스를 구현해서 등록하면 된다. 
이 컨버터 인터페이스는 모든 타입에 적용할 수 있다. 
필요하면 X Y 타입으로 변환하는 컨버터 인터페이스를 만들고, 또 Y X 타입으로 변환하는 컨버터 인터페이스를 만들어서 등록하면 된다.
예를 들어서 문자로 `"true"` 가 오면 `Boolean` 타입으로 받고 싶으면 `String` -> `Boolean` 타입으로 변환되도록 
컨버터 인터페이스를 만들어서 등록하고, 반대로 적용하고 싶으면 `Boolean` -> `String` 타입으로 변환되도록 컨버터를 추가로 만들어서 등록하면 된다.

> 참고
> 
> 과거에는 `PropertyEditor` 라는 것으로 타입을 변환했다. `PropertyEditor` 는 동시성 문제가 있어서
> 타입을 변환할 때 마다 객체를 계속 생성해야 하는 단점이 있다. 지금은 `Converter` 의 등장으로 해당 문제들이 해결되었고,
> 기능 확장이 필요하면 `Converter` 를 사용하면 된다.

## 타입 컨버터 - Converter

타입 컨버터를 사용하려면 `org.springframework.core.convert.converter.Converter` 인터페이스를 구현하면 된다.


> 주의
> 
> `Converter` 라는 이름의 인터페이스가 많으니 조심해야 한다. 
> `org.springframework.core.convert.converter.Converter` 를 사용해야 한다.


#### 컨버터 인터페이스

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {
    T convert(S source);
}
```

#### StringToIntegerConverter - 문자를 숫자로 변환하는 타입 컨버터

```java
package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;


@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        log.info("convert source={}", source);
        Integer integer = Integer.valueOf(source);
        return integer;
    }
}
```


`String` -> `Integer` 로 변환하기 때문에 소스가 String 이 된다. 
이 문자를 `Integer.valueOf(source)` 를 사용해서 숫자로 변경한 다음에 변경된 숫자를 반환하면 된다.

#### `IntegerToStringConverter` - 숫자를 문자로 변환하는 타입 컨버터

```java
package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IntegerToStringConverter implements Converter<Integer, String> {

    @Override
    public String convert(Integer source) {
        log.info("convert source={}", source);
        return String.valueOf(source);
    }
}
```

이번에는 숫자를 문자로 변환하는 타입 컨버터이다. 앞의 컨버터와 반대의 일을 한다. 
이번에는 숫자가 입력되기 때문에 소스가 `Integer` 가 된다. `String.valueOf(source)` 를 사용해서 문자로 변경한 다음 변경된 문자를 반환하면 된다.

#### ConverterTest - 타입 컨버터 테스트 코드

```java
package hello.typeconverter.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    public void stringToInteger() throws Exception {
        //given
        StringToIntegerConverter converter = new StringToIntegerConverter();

        //when
        Integer result = converter.convert("10");

        //then
        assertThat(result).isEqualTo(10);
    }

    @Test
    public void intergerToString() throws Exception {
        //given
        IntegerToStringConverter converter = new IntegerToStringConverter();

        //when
        String result = converter.convert(10);

        //then
        assertThat(result).isEqualTo("10");
    }

}
```

#### 사용자 정의 타입 컨버터

127.0.0.1:8080 과 같은 IP, PORT를 입력하면 IpPort 객체로 변환하는 컨버터

#### IpPort

```java
package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 127.0.0.1:8080
 */
@Getter
@EqualsAndHashCode
public class IpPort {

    private String ip;
    private int port;

    public IpPort(String id, int port) {
        this.ip = id;
        this.port = port;
    }
}
```

롬복의 `@EqualsAndHashCode` 를 넣으면 모든 필드를 사용해서 `equals()` , `hashcode()` 를 생성한다. 
따라서 모든 필드의 값이 같다면 `a.equals(b)` 의 결과가 참이 된다.

#### StringToIpPortConverter

```java
package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {


    @Override
    public IpPort convert(String source) {
        log.info("convert source={}", source);
        String[] split = source.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        return new IpPort(ip, port);
    }
}
```

`127.0.0.1:8080` 같은 문자를 입력하면 `IpPort` 객체를 만들어 반환한다.

#### IpPortToStringConverter

```java
package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);
        return source.getIp() + ":" + source.getPort();
    }
}
```

#### ConverterTest - IpPort 컨버터 테스트 추가

```java
@Test
public void stringToIpPort() throws Exception {
    //given
    IpPortToStringConverter converter = new IpPortToStringConverter();

    //when
    IpPort source = new IpPort("127.0.0.1", 8080);
    String result = converter.convert(source);

    //then
    assertThat(result).isEqualTo("127.0.0.1:8080");
}

@Test
public void ipPortToString() throws Exception {
    //given
    StringToIpPortConverter converter = new StringToIpPortConverter();

    //when
    String source = "127.0.0.1:8080";
    IpPort result = converter.convert(source);

    //then
    assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
}
```

> 참고
> 
> 스프링은 용도에 따라 다양한 방식의 타입 컨버터를 제공한다.
> 
> `Converter` 기본 타입 컨버터
> `ConverterFactory` 전체 클래스 계층 구조가 필요할 때 
> `GenericConverter` 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능 
> `ConditionalGenericConverter` 특정 조건이 참인 경우에만 실행
> 
> https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#core- convert

> 참고
> 
> 스프링은 문자, 숫자, 불린, Enum등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다. 
> IDE에서 `Converter` , `ConverterFactory` , `GenericConverter` 의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.
