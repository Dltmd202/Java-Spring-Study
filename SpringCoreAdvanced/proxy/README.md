# 4. 프록시 패턴과 데코레이터 패턴

## 예제 프로젝트 만들기 v1


### 예제는 크게 3가지 상황

* v1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록 
* v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록 
* v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록


## v1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록

### OrderRepositoryV1

```java
package hello.proxy.app.v1;

public interface OrderRepositoryV1 {
    void save(String itemId);
}
```

### OrderRepositoryV1Impl

```java
package hello.proxy.app.v1;

public class OrderRepositoryV1Impl implements OrderRepositoryV1 {

    @Override
    public void save(String itemId) {
        if(itemId.equals("ex")){
            throw new IllegalArgumentException("예외 발생");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```


### OrderServiceV1

```java
package hello.proxy.app.v1;

public interface OrderServiceV1 {
    void orderItem(String itemId);
}
```

### OrderServiceV1Impl

```java
package hello.proxy.app.v1;

public class OrderServiceV1Impl implements OrderServiceV1{

    private final OrderRepositoryV1 orderRepository;

    public OrderServiceV1Impl(OrderRepositoryV1 orderRepositoryV1) {
        this.orderRepository = orderRepositoryV1;
    }

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}

```


### OrderControllerV1

```java
package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping
@ResponseBody
public interface OrderControllerV1 {

    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
```


* `@RequestMapping` : 스프링MVC는 타입에 `@Controller` 또는 `@RequestMapping` 애노테이션이 있어야 스프링 컨트롤러로 인식한다.
  그리고 스프링 컨트롤러로 인식해야, HTTP URL이 매핑되고 동작한다. 이 애노테이션은 인터페이스에 사용해도 된다.

* `@ResponseBody` : HTTP 메시지 컨버터를 사용해서 응답한다. 이 애노테이션은 인터페이스에 사용해도 된다.

* `@RequestParam("itemId") String itemId` : 인터페이스에는 `@RequestParam("itemId")` 의 값을 생략하면 `itemId`
  단어를 컴파일 이후 자바 버전에 따라 인식하지 못할 수 있다. 인터페이스에서는 꼭 넣어주자. 클래스에는 생략해도 대부분 잘 지원된다.

* 코드를 보면 `request()` , `noLog()` 두 가지 메서드가 있다. `request()` 는 `LogTrace` 를 적용할 대상이고,
  `noLog()` 는 단순히 `LogTrace` 를 적용하지 않을 대상이다.


### OrderControllerV1Impl

```java
package hello.proxy.app.v1;

public class OrderControllerV1Impl implements OrderControllerV1{

    private final OrderServiceV1 orderService;

    public OrderControllerV1Impl(OrderServiceV1 orderService) {
        this.orderService = orderService;
    }

    @Override
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @Override
    public String noLog() {
        return "ok";
    }
}
```


### AppV1Config

```java
package hello.proxy;

import hello.proxy.app.v1.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV1Config {

    @Bean
    public OrderControllerV1 orderControllerV1(){
        return new OrderControllerV1Impl(orderServiceV1());
    }

    @Bean
    public OrderServiceV1 orderServiceV1() {
        return new OrderServiceV1Impl(orderRepositoryV1());
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        return new OrderRepositoryV1Impl();
    }
}
```


### ProxyApplication - 코드 추가

```java
package hello.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(AppV1Config.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
```

* `@Import(AppV1Config.class)` : 클래스를 스프링 빈으로 등록한다. 여기서는 `AppV1Config.class` 를 
  스프링 빈으로 등록한다. 일반적으로 `@Configuration` 같은 설정 파일을 등록할 때 사용하지만, 스프링 빈을 등록할 때도 사용할 수 있다.

* `@SpringBootApplication(scanBasePackages = "hello.proxy.app")` : `@ComponentScan` 의 기능과 같다. 
  컴포넌트 스캔을 시작할 위치를 지정한다. 이 값을 설정하면 해당 패키지와 그 하위 패키지를 컴포넌트 스캔한다. 
  이 값을 사용하지 않으면 `ProxyApplication` 이 있는 패키지와 그 하위 패키지를 스캔한다.

> 주의
>
> `@Configuration` 은 내부에 `@Component` 애노테이션을 포함하고 있어서 컴포넌트 스캔의 대상이 된다. 
> 따라서 컴포넌트 스캔에 의해 `hello.proxy.config` 위치의 설정 파일들이 스프링 빈으로 자동 등록 되지 않도록 컴포넌스 스캔의 시작 위치를 
> `scanBasePackages=hello.proxy.app` 로 설정해야 한다.

## 예제 프로젝트 만들기 v2

### OrderRepositoryV2

```java
package hello.proxy.app.v2;

public class OrderRepositoryV2 {

    public void save(String itemId) {
        if(itemId.equals("ex")){
            throw new IllegalArgumentException("예외 발생");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### OrderServiceV2

```java
package hello.proxy.app.v2;

public class OrderServiceV2 {
    private final OrderRepositoryV2 orderRepository;

    public OrderServiceV2(OrderRepositoryV2 orderRepositoryV1) {
        this.orderRepository = orderRepositoryV1;
    }

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
```

### OrderControllerV2

```java
package hello.proxy.app.v2;

import hello.proxy.app.v1.OrderRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping
@ResponseBody
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;

    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
```

* `@RequestMapping` : 스프링MVC는 타입에 `@Controller` 또는 `@RequestMapping` 애노테이션이 있어야 스프링 컨트롤러로 인식한다. 
  그리고 스프링 컨트롤러로 인식해야, HTTP URL이 매핑되고 동작한다. 그런데 여기서는 `@Controller` 를 사용하지 않고, `@RequestMapping`
  애노테이션을 사용했다. 그 이유는 `@Controller` 를 사용하면 자동 컴포넌트 스캔의 대상이 되기 때문이다. 
  여기서는 컴포넌트 스캔을 통한 자동 빈 등록이 아니라 수동 빈 등록을 하는 것이 목표다. 
  따라서 컴포넌트 스캔과 관계 없는 `@RequestMapping` 를 타입에 사용했다.


### AppV2Config

```java
package hello.proxy;

import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppV2Config {

    @Bean
    public OrderControllerV2 orderControllerV2(){
        return new OrderControllerV2(orderServiceV2());
    }

    @Bean
    public OrderServiceV2 orderServiceV2() {
        return new OrderServiceV2(orderRepositoryV2());
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2() {
        return new OrderRepositoryV2();
    }
}
```

### ProxyApplication

```java
package hello.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class)
@Import({AppV2Config.class, AppV1Config.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
```


## 예제 프로젝트 V3

#### v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록

#### OrderRepositoryV3

```java
package hello.proxy.app.v3;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV3 {

    public void save(String itemId) {
        if(itemId.equals("ex")){
            throw new IllegalArgumentException("예외 발생");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```


#### OrderServiceV3

```java
package hello.proxy.app.v3;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;

    public OrderServiceV3(OrderRepositoryV3 orderRepositoryV3) {
        this.orderRepository = orderRepositoryV3;
    }

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
```


#### OrderControllerV3

```java
package hello.proxy.app.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;

    public OrderControllerV3(OrderServiceV3 orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/v3/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v3/no-log")
    public String noLog() {
        return "ok";
    }
}
```

`ProxyApplication` 에서 `@SpringBootApplication(scanBasePackages = "hello.proxy.app")` 를 사용했고, 
각각 `@RestController` , `@Service` , `@Repository` 애노테이션을 가지고 있기 때문에 컴포넌트 스캔의 대상이 된다.


## 요구사항 추가

#### 기존 요구사항


* 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력 
* 애플리케이션의 흐름을 변경하면 안됨
  * 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨 
* 메서드 호출에 걸린 시간
* 정상 흐름과 예외 흐름 구분
  * 예외 발생시 예외 정보가 남아야 함 
* 메서드 호출의 깊이 표현
* HTTP 요청을 구분
  * HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  * 트랜잭션 ID (DB 트랜잭션X)


#### 하지만
하지만 이 요구사항을 만족하기 위해서 기존 코드를 많이 수정해야 한다. 코드 수정을 최소화 하기 위해
템플릿 메서드 패턴과 콜백 패턴도 사용했지만, 결과적으로 로그를 남기고 싶은 클래스가 수백개라면 수백개의 클래스를 모두 고쳐야한다. 
로그를 남길 때 기존 원본 코드를 변경해야 한다는 사실 그 자체가 개발자에게는 가장 큰 문제로 남는다.


#### 요구사항 추가
* 원본 코드를 전혀 수정하지 않고, 로그 추적기를 적용해라. 
* 특정 메서드는 로그를 출력하지 않는 기능
  * 보안상 일부는 로그를 출력하면 안된다.
* 다음과 같은 다양한 케이스에 적용할 수 있어야 한다.
  * v1 - 인터페이스가 있는 구현 클래스에 적용 
  * v2 - 인터페이스가 없는 구체 클래스에 적용 
  * v3 - 컴포넌트 스캔 대상에 기능 적용


## 프록시, 프록시 패턴, 데코레이터 패턴

클라이언트와 서버

![](res/img.png)



클라이언트(`Client`)와 서버(`Server`)라고 하면 개발자들은 보통 서버 컴퓨터를 생각한다.


사실 클라이언트와 서버의 개념은 상당히 넓게 사용된다. 클라이언트는 의뢰인이라는 뜻이고, 서버는 '서비스나 상품을 제공하는 사람이나 물건'을 뜻한다.
따라서 클라이언트와 서버의 기본 개념을 정의하면 클라이언트는 서버에 필요한 것을 요청하고, 서버는 클라이언트의 요청을 처리하는 것이다.


이 개념을 우리가 익숙한 컴퓨터 네트워크에 도입하면 클라이언트는 웹 브라우저가 되고, 요청을 처리하는 서버는 웹 서버가 된다.


이 개념을 객체에 도입하면, 요청하는 객체는 클라이언트가 되고, 요청을 처리하는 객체는 서버가 된다.


#### 직접 호출과 간접 호출

![](res/img_1.png)


클라이언트와 서버 개념에서 일반적으로 클라이언트가 서버를 직접 호출하고, 처리 결과를 직접 받는다. 
이것을 직접 호출이라 한다.


![](res/img_2.png)



그런데 클라이언트가 요청한 결과를 서버에 직접 요청하는 것이 아니라 어떤 대리자를 통해서 대신 간접적으로 서버에 요청할 수 있다. 
예를 들어서 내가 직접 마트에서 장을 볼 수도 있지만, 누군가에게 대신 장을 봐달라고 부탁할 수도 있다.
여기서 대신 장을 보는 대리자를 영어로 프록시(Proxy)라 한다.


#### 대체 가능


그런데 여기까지 듣고 보면 아무 객체나 프록시가 될 수 있는 것 같다.
객체에서 프록시가 되려면, 클라이언트는 서버에게 요청을 한 것인지, 프록시에게 요청을 한 것인지 조차 몰라야 한다.
쉽게 이야기해서 서버와 프록시는 같은 인터페이스를 사용해야 한다. 
그리고 클라이언트가 사용하는 서버 객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작할 수 있어야 한다.


#### 서버와 프록시가 같은 인터페이스 사용

![](res/img_3.png)


클래스 의존관계를 보면 클라이언트는 서버 인터페이스(`ServerInterface`)에만 의존한다. 
그리고 서버와 프록시가 같은 인터페이스를 사용한다. 
따라서 DI를 사용해서 대체 가능하다.


![](res/img_4.png)



![](res/img_5.png)

이번에는 런타임 객체 의존 관계를 살펴보자. 런타임(애플리케이션 실행 시점)에 클라이언트 객체에 DI를
사용해서 `Client -> Server` 에서 `Client -> Proxy` 로 객체 의존관계를 변경해도 클라이언트 코드를 전혀 변경하지 않아도 된다. 
클라이언트 입장에서는 변경 사실 조차 모른다. DI를 사용하면 클라이언트 코드의 변경 없이 유연하게 프록시를 주입할 수 있다.



### 프록시의 주요 기능

프록시를 통해서 할 수 있는 일은 크게 2가지로 구분할 수 있다.


* 접근 제어
  * 권한에 따른 접근 차단 
  * 캐싱
  * 지연 로딩
* 부가 기능 추가
  * 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다. 
  * 예) 요청 값이나, 응답 값을 중간에 변형한다.
  * 예) 실행 시간을 측정해서 추가 로그를 남긴다.


### GOF 디자인 패턴

둘다 프록시를 사용하는 방법이지만 GOF 디자인 패턴에서는 이 둘을 의도(intent)에 따라서 프록시 패턴과 데코레이터 패턴으로 구분한다.


* 프록시 패턴: 접근 제어가 목적 
* 데코레이터 패턴: 새로운 기능 추가가 목적


둘다 프록시를 사용하지만, 의도가 다르다는 점이 핵심이다. 용어가 프록시 패턴이라고 해서 이 패턴만 프록시를 사용하는 것은 아니다. 
데코레이터 패턴도 프록시를 사용한다.


## 프록시 패턴 - 예제 코드1


### 프록시 패턴 - 예제 코드 작성


![](res/img_6.png)


![](res/img_7.png)


#### Subject 인터페이스

```java
package hello.proxy.proxy.code;

public interface Subject {
  String operation();
}
```

예제에서 `Subject` 인터페이스는 단순히 `operation()` 메서드 하나만 가지고 있다.


#### RealSubject

```java
package hello.proxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealSubject implements Subject {
  @Override
  public String operation() {
    log.info("실제 객체 호출");
    sleep(1000);
    return "data";
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
```

`RealSubject` 는 `Subject` 인터페이스를 구현했다. `operation()` 은 데이터 조회를 시뮬레이션 하기 위해 1초 쉬도록 했다. 
예를 들어서 데이터를 DB나 외부에서 조회하는데 1초가 걸린다고 생각하면 된다. 호출할 때 마다 시스템에 큰 부하를 주는 데이터 조회한다고 가정한다.


#### ProxyPatternClient

```java
package hello.proxy.proxy.code;

public class ProxyPatternClient {
  private Subject subject;

  public ProxyPatternClient(Subject subject){
    this.subject = subject;
  }

  public void execute(){
    subject.operation();
  }
}

```

`Subject` 인터페이스에 의존하고, `Subject` 를 호출하는 클라이언트 코드이다. 
`execute()` 를 실행하면 `subject.operation()` 를 호출한다.


#### ProxyPatternTest

```java
package hello.proxy.proxy;

import hello.proxy.proxy.code.ProxyPatternClient;
import hello.proxy.proxy.code.RealSubject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {
    @Test
    public void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }
}
```

테스트 코드에서는 `client.execute()` 를 3번 호출한다. 데이터를 조회하는데 1초가 소모되므로 총 3 초의 시간이 걸린다.


#### `lient.execute()`을 3번 호출하면 다음과 같이 처리된다.


* 1. `client -> realSubject` 를 호출해서 값을 조회한다. (1초) 
* 2. `client -> realSubject` 를 호출해서 값을 조회한다. (1초) 
* 3. `client -> realSubject` 를 호출해서 값을 조회한다. (1초)


그런데 이 데이터가 한번 조회하면 변하지 않는 데이터라면 어딘가에 보관해두고 이미 조회한 데이터를 사용하는 것이 성능상 좋다. 
이런 것을 캐시라고 한다.
프록시 패턴의 주요 기능은 접근 제어이다. 캐시도 접근 자체를 제어하는 기능 중 하나이다.


## 프록시 패턴 - 예제 코드2

![](res/img_8.png)


![](res/img_9.png)


#### CacheProxy

```java
package hello.proxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject{

    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if(cacheValue == null){
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
```

프록시도 실제 객체와 그 모양이 같아야 하기 때문에 Subject 인터페이스를 구현해야 한다.

* `private Subject target` : 클라이언트가 프록시를 호출하면 프록시가 최종적으로 실제 객체를 호출해야 한다. 
  따라서 내부에 실제 객체의 참조를 가지고 있어야 한다. 이렇게 프록시가 호출하는 대상을 `target` 이라 한다. 
* `operation()` : 구현한 코드를 보면 `cacheValue` 에 값이 없으면 실제 객체(`target`)를 호출해서 값을 구한다.
  그리고 구한 값을 `cacheValue` 에 저장하고 반환한다. 만약 `cacheValue` 에 값이 있으면 실제 객체를 전혀 호출하지 않고,
  캐시 값을 그대로 반환한다. 따라서 처음 조회 이후에는 캐시(`cacheValue`) 에서 매우 빠르게 데이터를 조회할 수 있다.


#### ProxyPatternTest - cacheProxyTest() 추가

```java
@Test
public void cacheProxyTest() {
    RealSubject realSubject = new RealSubject();
    CacheProxy cacheProxy = new CacheProxy(realSubject);
    ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
    client.execute();
    client.execute();
    client.execute();
}
```

#### cacheProxyTest()
`realSubject` 와 `cacheProxy` 를 생성하고 둘을 연결한다. 결과적으로 `cacheProxy` 가 `realSubject` 를 참조하는 
런타임 객체 의존관계가 완성된다. 그리고 마지막으로 `client` 에 `realSubject` 가 아닌 `cacheProxy` 를 주입한다. 
이 과정을 통해서 `client` -> `cacheProxy` -> `realSubject` 런타임 객체 의존 관계가 완성된다.


`cacheProxyTest()` 는 `client.execute()` 을 총 3번 호출한다. 이번에는 클라이언트가 실제 `realSubject` 를 
호출하는 것이 아니라 `cacheProxy` 를 호출하게 된다.


#### 실행 결과

```text
23:46:18.827 [Test worker] INFO hello.proxy.proxy.code.CacheProxy - 프록시 호출
23:46:18.828 [Test worker] INFO hello.proxy.proxy.code.RealSubject - 실제 객체 호출
23:46:19.833 [Test worker] INFO hello.proxy.proxy.code.CacheProxy - 프록시 호출
23:46:19.834 [Test worker] INFO hello.proxy.proxy.code.CacheProxy - 프록시 호출
```


#### client.execute()을 3번 호출하면 다음과 같이 처리된다.


* 1. client의 cacheProxy 호출 -> cacheProxy에 캐시 값이 없다. -> realSubject를 호출, 결과를 캐시에 저장 (1초)
* 2. client의 cacheProxy 호출 -> cacheProxy에 캐시 값이 있다. -> cacheProxy에서 즉시 반환 (0초) 
* 3. client의 cacheProxy 호출 -> cacheProxy에 캐시 값이 있다. -> cacheProxy에서 즉시 반환 (0초) 

결과적으로 캐시 프록시를 도입하기 전에는 3초가 걸렸지만, 캐시 프록시 도입 이후에는 최초에 한번만 1 초가 걸리고, 이후에는 거의 즉시 반환한다.


## 데코레이터 패턴 - 예제 코드1

![](res/img_10.png)


![](res/img_11.png)


#### Component 인터페이스

```java
package hello.proxy.decorator.code;

public interface Component {
  String operation();
}
```

`Component` 인터페이스는 단순히 `String operation()` 메서드를 가진다.


#### RealComponent

```java
package hello.proxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component{

    @Override
    public String operation() {
        log.info("RealComponent 생성");
        return "data";
    }
}
```


* `RealComponent` 는 `Component` 인터페이스를 구현한다. 
* `operation()` : 단순히 로그를 남기고 "data" 문자를 반환한다.


#### DecoratorPatternClient

```java
package hello.proxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecoratorPatternClient {

    private Component component;

    public DecoratorPatternClient(Component component) {
        this.component = component;
    }

    public void execute(){
        String result = component.operation();
        log.info("result = {}", result);
    }
}
```

* 클라이언트 코드는 단순히 `Component` 인터페이스를 의존한다.
* `execute()` 를 실행하면 `component.operation()` 을 호출하고, 그 결과를 출력한다.


#### DecoratorPatternTest

```java
package hello.proxy.decorator;

import hello.proxy.decorator.code.DecoratorPatternClient;
import hello.proxy.decorator.code.RealComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    public void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient decoratorPatternClient = new DecoratorPatternClient(realComponent);
        decoratorPatternClient.execute();
    }
}
```

테스트 코드는 `client -> realComponent` 의 의존관계를 설정하고, `client.execute()` 를 호출한다.


#### 실행 결과

```text
22:59:31.759 [Test worker] INFO hello.proxy.decorator.code.RealComponent - RealComponent 생성
22:59:31.762 [Test worker] INFO hello.proxy.decorator.code.DecoratorPatternClient - result = data
```

#### 데코레이터 패턴 - 예제 코드2



#### 부가 기능 추가

프록시를 활용해서 부가 기능을 추가한다. 프록시로 부가 기능을 추가하는 것을 데코레이터 패턴이라 한다.


데코레이터 패턴: 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다. 

* 예) 요청 값이나, 응답 값을 중간에 변형한다. 
* 예) 실행 시간을 측정해서 추가 로그를 남긴다.


#### 응답 값을 꾸며주는 데코레이터

응답 값을 꾸며주는 데코레이터 프록시를 만들어보자.


![](res/img_12.png)


![](res/img_13.png)


#### MessageDecorator

```java
package hello.proxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component{

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        String result = component.operation();
        String decoResult = "*****" + result + "*****";

        log.info("MessageDecorator 꾸미기 적용 전 = {}, 적용 후 = {}", result, decoResult);
        return decoResult;
    }
}
```


`MessageDecorator` 는 `Component` 인터페이스를 구현한다.
프록시가 호출해야 하는 대상을 `component` 에 저장한다.
`operation()` 을 호출하면 프록시와 연결된 대상을 호출(`component.operation()`) 하고, 그 응답 값에 
`*****` 을 더해서 꾸며준 다음 반환한다.


* 꾸미기 전: `data`
* 꾸민 후 : `*****data*****`


#### DecoratorPatternTest - 추가

```java
@Test
public void decorator1() {
    RealComponent realComponent = new RealComponent();
    MessageDecorator messageDec orator = new MessageDecorator(realComponent);
    DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
    client.execute();
}
```

`client -> messageDecorator -> realComponent` 의 객체 의존 관계를 만들고 `client.execute()` 를 호출한다.


#### 실행 결과

실행 결과를 보면 `MessageDecorator` 가 `RealComponent` 를 호출하고 반환한 응답 메시지를 꾸며서 반환한 것을 확인할 수 있다.


## 데코레이터 패턴 - 예제 코드3

#### 실행 시간을 측정하는 데코레이터

![](res/img_14.png)


![](res/img_15.png)


#### TimeDecorator

```java
package hello.proxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeDecorator implements Component{

    private Component component;

    public TimeDecorator(Component component){
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = component.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);
        return result;
    }
}
```


`TimeDecorator` 는 실행 시간을 측정하는 부가 기능을 제공한다. 
대상을 호출하기 전에 시간을 가지고 있다가, 대상의 호출이 끝나면 호출 시간을 로그로 남겨준다.



#### DecoratorPatternTest - 추가

```java
@Test
public void decorator2() {
    Component realComponent = new RealComponent();
    Component messageDecorator = new MessageDecorator(realComponent);
    Component timeDecorator = new TimeDecorator(messageDecorator);
    DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
    client.execute();
}
```


`client -> timeDecorator -> messageDecorator -> realComponent` 의 객체 의존관계를 설정하고, 실행한다.


#### 실행 결과

```text
17:20:56.016 [Test worker] INFO hello.proxy.decorator.code.TimeDecorator - TimeDecorator 실행
17:20:56.017 [Test worker] INFO hello.proxy.decorator.code.MessageDecorator - MessageDecorator 실행
17:20:56.017 [Test worker] INFO hello.proxy.decorator.code.RealComponent - RealComponent 생성
17:20:56.021 [Test worker] INFO hello.proxy.decorator.code.MessageDecorator - MessageDecorator 꾸미기 적용 전 = data, 적용 후 = *****data*****
17:20:56.022 [Test worker] INFO hello.proxy.decorator.code.TimeDecorator - TimeDecorator 종료 resultTime=5ms
17:20:56.022 [Test worker] INFO hello.proxy.decorator.code.DecoratorPatternClient - result = *****data*****
```


실행 결과를 보면 `TimeDecorator` 가 `MessageDecorator` 를 실행하고 실행 시간을 측정해서 출력한 것을 확인할 수 있다.



## 프록시 패턴과 데코레이터 패턴 정리


GOF 데코레이터 패턴

![](res/img_16.png)


여기서 생각해보면 `Decorator` 기능에 일부 중복이 있다. 꾸며주는 역할을 하는 `Decorator` 들은 스스로 존재할 수 없다. 
항상 꾸며줄 대상이 있어야 한다. 따라서 내부에 호출 대상인 `component` 를 가지고 있어야 한다. 그리고 `component` 를 항상 호출해야 한다.
이 부분이 중복이다. 이런 중복을 제거하기 위해 `component` 를 속성으로 가지고 있는 `Decorator` 라는 추상 클래스를 만드는 방법도 고민할 수 있다.
이렇게 하면 추가로 클래스 다이어그램에서 어떤 것이 실제 컴포넌트 인지, 데코레이터인지 명확하게 구분할 수 있다. 




#### 의도(intent)

사실 프록시 패턴과 데코레이터 패턴은 그 모양이 거의 같고, 상황에 따라 정말 똑같을 때도 있다. 그러면 둘을 어떻게 구분하는 것일까?
디자인 패턴에서 중요한 것은 해당 패턴의 겉모양이 아니라 그 패턴을 만든 의도가 더 중요하다. 따라서 의도에 따라 패턴을 구분한다.

* 프록시 패턴의 의도: 다른 개체에 대한 접근을 제어하기 위해 대리자를 제공
* 데코레이터 패턴의 의도: 객체에 추가 책임(기능)을 동적으로 추가하고, 기능 확장을 위한 유연한 대안 제공



## 인터페이스 기반 프록시 - 적용

#### 프록시를 사용하면 기존 코드를 전혀 수정하지 않고 로그 추적 기능을 도입할 수 있다.


#### V1 기본 클래스 의존 관계

![](res/img_17.png)


#### V1 런타임 객체 의존 관계

![](res/img_18.png)


#### 여기에 로그 추적용 프록시를 추가하면 다음과 같다.


#### V1 프록시 의존 관계 추가


![](res/img_19.png)

`Controller` , `Service` , `Repository` 각각 인터페이스에 맞는 프록시 구현체를 추가한다. (그림에서 리포지토리는 생략했다.)


#### V1 프록시 런타임 객체 의존 관계

![](res/img_20.png)


그리고 애플리케이션 실행 시점에 프록시를 사용하도록 의존 관계를 설정해주어야 한다. 이 부분은 빈을 등록하는 설정 파일을 활용하면 된다.


#### OrderRepositoryInterfaceProxy

```java
package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderRepository.request()");
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}
```

* 프록시를 만들기 위해 인터페이스를 구현하고 구현한 메서드에 `LogTrace` 를 사용하는 로직을 추가한다. 
  지금까지는 `OrderRepositoryImpl` 에 이런 로직을 모두 추가해야했다. 프록시를 사용한 덕분에 이 부분을 프록시가 대신 처리해준다.
  따라서 `OrderRepositoryImpl` 코드를 변경하지 않아도 된다.

* `OrderRepositoryV1 target` : 프록시가 실제 호출할 원본 리포지토리의 참조를 가지고 있어야 한다.


#### OrderServiceInterfaceProxy

```java
package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1 {

    private final OrderServiceV1 target;
    private final LogTrace logTrace;

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.request()");
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
```


#### OrderControllerInterfaceProxy

```java
package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace logTrace;

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderController.request()");
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
```

`noLog()` 메서드는 로그를 남기지 않아야 한다. 따라서 별도의 로직 없이 단순히 `target` 을 호출하면 된다.


#### InterfaceProxyConfig

```java
package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace){
        OrderControllerV1 controllerImpl = new OrderControllerV1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }
}
```

`LogTrace` 가 아직 스프링 빈으로 등록되어 있지 않은데, 이 부분은 바로 다음에 등록할 것이다.


#### V1 프록시 런타임 객체 의존 관계 설정

* 이제 프록시의 런타임 객체 의존 관계를 설정하면 된다. 기존에는 스프링 빈이 `orderControlerV1Impl` , `orderServiceV1Impl` 같은 
  실제 객체를 반환했다. 하지만 이제는 프록시를 사용해야한다. 따라서 프록시를 생성하고 프록시를 실제 스프링 빈 대신 등록한다. 
  실제 객체는 스프링 빈으로 등록하지 않는다. 
* 프록시는 내부에 실제 객체를 참조하고 있다. 예를 들어서 `OrderServiceInterfaceProxy` 는 내부에 실제 대상 
  객체인 `OrderServiceV1Impl` 을 가지고 있다.
* 정리하면 다음과 같은 의존 관계를 가지고 있다. 
  * `proxy -> target `
  * `orderServiceInterfaceProxy -> orderServiceV1Impl` 
* 스프링 빈으로 실제 객체 대신에 프록시 객체를 등록했기 때문에 앞으로 스프링 빈을 주입 받으면 실제 객체 대신에 프록시 객체가 주입된다. 
* 실제 객체가 스프링 빈으로 등록되지 않는다고 해서 사라지는 것은 아니다. 프록시 객체가 실제 객체를 참조하기 때문에 프록시를 통해서 실제 객체를 호출할 수 있다. 
  쉽게 이야기해서 프록시 객체 안에 실제 객체가 있는 것이다.


![](res/img_21.png)

`AppV1Config` 를 통해 프록시를 적용하기 전
* 실제 객체가 스프링 빈으로 등록된다. 빈 객체의 마지막에 `@x0..` 라고 해둔 것은 인스턴스라는 뜻이다.


![](res/img_22.png)

`InterfaceProxyConfig` 를 통해 프록시를 적용한 후

* 스프링 컨테이너에 프록시 객체가 등록된다. 스프링 컨테이너는 이제 실제 객체가 아니라 프록시 객체를 스프링 빈으로 관리한다. 
* 이제 실제 객체는 스프링 컨테이너와는 상관이 없다. 실제 객체는 프록시 객체를 통해서 참조될 뿐이다. 
* 프록시 객체는 스프링 컨테이너가 관리하고 자바 힙 메모리에도 올라간다. 반면에 실제 객체는 자바 힙 메모리에는 올라가지만 
  스프링 컨테이너가 관리하지는 않는다.

![](res/img_23.png)


#### ProxyApplication

```java
package hello.proxy;

import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class)
//@Import({AppV2Config.class, AppV1Config.class})
@Import(InterfaceProxyConfig.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace(){
		return new ThreadLocalLogTrace();
	}
}
```

* `@Bean` : 먼저 `LogTrace` 스프링 빈 추가를 먼저 해주어야 한다. 이것을 여기에 등록한 이유는 앞으로 사용할 모든 예제에서 함께 사용하기 위해서다.
* `@Import(InterfaceProxyConfig.class)` : 프록시를 적용한 설정 파일을 사용하자. 
  * `//@Import({AppV1Config.class, AppV2Config.class}) 주석 처리하자.`

#### 실행 결과 - 로그

```text
[65b39db2] OrderController.request()
[65b39db2] |-->OrderService.orderItem()
[65b39db2] |   |-->OrderRepository.request()
[65b39db2] |   |<--OrderRepository.request() time=1002ms
[65b39db2] |<--OrderService.orderItem() time=1002ms
[65b39db2] OrderController.request() time=1003ms
```

실행 결과를 확인해보면 로그 추적 기능이 프록시를 통해 잘 동작하는 것을 확인할 수 있다.

프록시와 DI 덕분에 원본 코드를 전혀 수정하지 않고, 로그 추적기를 도입할 수 있었다. 물론 너무 많은 프록시 클래스를 만들어야 하는 단점이 있기는 하다. 
이 부분은 나중에 해결하기로 하고, 우선은 v2 - 인터페이스가 없는 구체 클래스에 프록시를 어떻게 적용할 수 있는지 알아보자.


## 구체 클래스 기반 프록시 - 예제1


### ConcreteLogic

```java
package hello.proxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteLogic {

    public String operation() {
        log.info("ConcreteLogic 실헹");
        return "data";
    }
}
```


`ConcreteLogic` 은 인터페이스가 없고, 구체 클래스만 있다. 여기에 프록시를 도입해야 한다.


![](res/img_24.png)

![](res/img_25.png)

### ConcreteClient

```java
package hello.proxy.concreteproxy.code;

public class ConcreteClient {

    private ConcreteLogic concreteLogic;

    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute(){
        concreteLogic.operation();
    }
}
```


### ConcreteProxyTest

```java
package hello.proxy.concreteproxy;

import hello.proxy.concreteproxy.code.ConcreteClient;
import hello.proxy.concreteproxy.code.ConcreteLogic;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {

    @Test
    public void noProxy() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }
}
```

## 구체 클래스 기반 프록시 - 예제2

### 클래스 기반 프록시 도입
지금까지 인터페이스를 기반으로 프록시를 도입했다. 그런데 자바의 다형성은 인터페이스를 구현하든, 클래스를 상속하든 상위 타입만 맞으면
다형성이 적용된다. 인터페이스가 없어도 프록시를 만들수 있다는 뜻이다. 

그래서 인터페이스가 아니라 클래스를 기반으로 상속을 받아서 프록시를 만들어본다.

![](res/img_26.png)


![](res/img_27.png)


### TimeProxy

```java
package hello.proxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic{

    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = concreteLogic.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);
        return result;
    }
}
```

`TimeProxy` 프록시는 시간을 측정하는 부가 기능을 제공한다. 그리고 인터페이스가 아니라 클래스인 
`ConcreteLogic` 를 상속 받아서 만든다.


#### ConcreteProxyTest - addProxy() 추가

```java
@Test
void addProxy() {
    ConcreteLogic concreteLogic = new ConcreteLogic();
    TimeProxy timeProxy = new TimeProxy(concreteLogic);
    ConcreteClient client = new ConcreteClient(timeProxy);
    client.execute();
}
```

여기서 핵심은 `ConcreteClient` 의 생성자에 `concreteLogic` 이 아니라 `timeProxy` 를 주입하는 부분이다.
`ConcreteClient` 는 `ConcreteLogic` 을 의존하는데, 다형성에 의해 `ConcreteLogic` 에 
`concreteLogic` 도 들어갈 수 있고, `timeProxy` 도 들어갈 수 있다.


#### ConcreteLogic에 할당할 수 있는 객체

* `ConcreteLogic` = `concreteLogic` (본인과 같은 타입을 할당) 
* `ConcreteLogic` = `timeProxy` (자식 타입을 할당)

#### ConcreteClient 참고

```java
package hello.proxy.concreteproxy.code;

public class ConcreteClient {

    private ConcreteLogic concreteLogic;

    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute(){
        concreteLogic.operation();
    }
}
```

#### 실행

```text
22:30:58.918 [Test worker] INFO hello.proxy.concreteproxy.code.TimeProxy - TimeDecorator 실행
22:30:58.920 [Test worker] INFO hello.proxy.concreteproxy.code.ConcreteLogic - ConcreteLogic 실헹
22:30:58.920 [Test worker] INFO hello.proxy.concreteproxy.code.TimeProxy - TimeDecorator 종료 resultTime=0ms
```

실행 결과를 보면 인터페이스가 없어도 클래스 기반의 프록시가 잘 적용된 것을 확인할 수 있다.


> 참고
> 
> 자바 언어에서 다형성은 인터페이스나 클래스를 구분하지 않고 모두 적용된다. 
> 해당 타입과 그 타입의 하위 타입은 모두 다형성의 대상이 된다. 자바 언어의 너무 기본적인 내용을 이야기했지만,
> 인터페이스가 없어도 프록시가 가능하다는 것을 확실하게 집고 넘어갈 필요가 있어서 자세히 설명했다.


