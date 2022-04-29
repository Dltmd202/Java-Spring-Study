# 1. 예제 만들기

## 예제 프로젝트 만들기 - V0

#### OrderRepositoryV0

```java
package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

    public void save(String itemId){
        if(itemId.equals("ex")){
            throw new IllegalArgumentException("예외 발생");
        }
        sleep(1000);
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

#### OrderServiceV0

```java
package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV0 {

    private final OrderRepositoryV0 orderRepository;

    public void orderItem(String itemId){
        orderRepository.save(itemId);
    }



}
```

#### OrderControllerV0

```java
package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV0 {

    private final OrderServiceV0 orderService;

    @GetMapping("/v0/request")
    public String request(String itemId){
        orderService.orderItem(itemId);
        return "ok";
    }
}
```

* `@RestController` : 컴포넌트 스캔과 스프링 Rest 컨트롤러로 인식된다.
* `/v0/request` 메서드는 HTTP 파라미터로 `itemId` 를 받을 수 있다.

### 로그 추적기 - 요구사항 분석

#### 요구사항

* 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력 
* 애플리케이션의 흐름을 변경하면 안됨
  * 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨 
* 메서드 호출에 걸린 시간
* 정상 흐름과 예외 흐름 구분
  * 예외 발생시 예외 정보가 남아야 함
* 메서드 호출의 깊이 표현 
* HTTP 요청을 구분
  * HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  * 트랜잭션 ID (DB 트랜잭션X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

#### 예시

```text
정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] |   |-->OrderRepository.save()
[796bccd9] |   |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms

예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] | |-->OrderRepository.save() 
[b7119f27] | |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] |<X-OrderService.orderItem() time=10ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] OrderController.request() time=11ms ex=java.lang.IllegalStateException: 예외 발생!
```

> 참고
> 
> 모니터링 툴을 도입하면 많은 부분이 해결되지만, 지금은 학습이 목적


### 로그 추적기 V1 - 프로토타입 개발

애플리케이션의 모든 로직에 직접 로그를 남겨도 되지만, 그것보다는 더 효율적인 개발 방법이 필요하다.


특히 트랜잭션ID와 깊이를 표현하는 방법은 기존 정보를 이어 받아야 하기 때문에 
단순히 로그만 남긴다고 해결할 수 있는 것은 아니다.


먼저 로그 추적기를 위한 기반 데이터를 가지고 있는 TraceId , TraceStatus 클래스를 만든다.

#### TraceId

```java
package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id;
    private int level;

    public TraceId(){
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level){
        this.id = id;
        this.level = level;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId(){
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel(){
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
```

#### TraceId 클래스
로그 추적기는 트랜잭션ID와 깊이를 표현하는 방법이 필요하다.
여기서는 트랜잭션ID와 깊이를 표현하는 level을 묶어서 `TraceId` 라는 개념을 만들었다. 
`TraceId` 는 단순히 `id` (트랜잭션ID)와 `level` 정보를 함께 가지고 있다.

```
[796bccd9] OrderController.request() //트랜잭션ID:796bccd9, level:0 
[796bccd9] |-->OrderService.orderItem() //트랜잭션ID:796bccd9, level:1 
[796bccd9] | |-->OrderRepository.save()//트랜잭션ID:796bccd9, level:2
```

#### UUID

`TraceId` 를 처음 생성하면 `createId()` 를 사용해서 UUID를 만들어낸다. 
UUID가 너무 길어서 여기서는 앞 8자리만 사용한다. 이 정도면 로그를 충분히 구분할 수 있다. 
여기서는 이렇게 만들어진 값을 트랜잭션ID 로 사용한다.

```
ab99e16f-3cde-4d24-8241-256108c203a2 //생성된 UUID 
ab99e16f //앞 8자리만 사용
```

#### createNextId()

다음 `TraceId` 를 만든다. 예제 로그를 잘 보면 깊이가 증가해도 트랜잭션ID는 같다. 
대신에 깊이가 하나 증가한다.

실행 코드: `new TraceId(id, level + 1)`

```
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem() //트랜잭션ID가 같다. 깊이는 하나 증가한다.
```

따라서 `createNextId()` 를 사용해서 현재 `TraceId` 를 기반으로 
다음 `TraceId` 를 만들면 `id` 는 기존과 같고, `level` 은 하나 증가한다.


#### createPreviousId()


`createNextId()` 의 반대 역할을 한다.
`id` 는 기존과 같고, `level` 은 하나 감소한다.



#### isFirstLevel()
 
첫 번째 레벨 여부를 편리하게 확인할 수 있는 메서드

#### TraceStatus

```java
package hello.advanced.trace;

public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs;
    private String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
```


`TraceStatus` 클래스: 로그의 상태 정보를 나타낸다.


로그를 시작하면 끝이 있어야 한다.

```
[796bccd9] OrderController.request() //로그 시작
[796bccd9] OrderController.request() time=1016ms //로그 종료
```


`TraceStatus` 는 로그를 시작할 때의 상태 정보를 가지고 있다. 이 상태 정보는 로그를 종료할 때 사용된다.


* `traceId` : 내부에 트랜잭션ID와 level을 가지고 있다.
* `startTimeMs` : 로그 시작시간이다. 로그 종료시 이 시작 시간을 기준으로 시작~종료까지 전체 수행 시간을 구할 수 있다.
* `message` : 시작시 사용한 메시지이다. 이후 로그 종료시에도 이 메시지를 사용해서 출력한다. 


`TraceId` , `TraceStatus` 를 사용해서 실제 로그를 생성하고, 처리하는 기능을 개발해보자.


#### HelloTraceV1

```java
package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<x-";

    public TraceStatus begin(String message){
        TraceId traceId = new TraceId();
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}",traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public void end(TraceStatus status){
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e){
        complete(status, e);
    }
    
    private void complete(TraceStatus status, Exception e){
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if(e == null){
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
    }

    private static String addSpace(String prefix, int level){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
```


`HelloTraceV1` 을 사용해서 실제 로그를 시작하고 종료할 수 있다. 그리고 로그를 출력하고 실행시간도 측정할 수 있다.

* `@Component` : 싱글톤으로 사용하기 위해 스프링 빈으로 등록한다. 컴포넌트 스캔의 대상이 된다.

#### 공개 메서드 
로그 추적기에서 사용되는 공개 메서드는 다음 3가지 이다.


* `TraceStatus begin(String message)`
  * 로그를 시작한다.
  * 로그 메시지를 파라미터로 받아서 시작 로그를 출력한다. 
  * 응답 결과로 현재 로그의 상태인 `TraceStatus` 를 반환한다.
* `void end(TraceStatus status)`
  * 로그를 정상 종료한다.
  * 파라미터로 시작 로그의 상태( `TraceStatus` )를 전달 받는다. 이 값을 활용해서 실행 시간을 계산하고, 종료시에도 시작할 때와 동일한 로그 메시지를 출력할 수 있다.
  * 정상 흐름에서 호출한다.
* `void exception(TraceStatus status, Exception e)`
  * 로그를 예외 상황으로 종료한다.
  * `TraceStatus` , `Exception` 정보를 함께 전달 받아서 실행시간, 예외 정보를 포함한 결과 로그를 출력한다.
  * 예외가 발생했을 때 호출한다.


#### 비공개 메서드

* `complete(TraceStatus status, Exception e)`
  * `end()` , `exception()` , 의 요청 흐름을 한곳에서 편리하게 처리한다. 실행 시간을 측정하고 로그를 남긴다.

* `String addSpace(String prefix, int level)` : 다음과 같은 결과를 출력한다.
  * prefix: `-->`
    * level 0:
    * level 1: `|-->`
    * level 2: `| |-->`
  * prefix: `<--`
    * level 0:
    * level 1: `|<--`
    * level 2: `| |<--`
  * prefix: `<X-`
    * level 0:
    * level 1: `|<X-`
    * level 2: `| |<X-`


참고로 `HelloTraceV1` 는 아직 모든 요구사항을 만족하지는 못한다. 

#### HelloTraceV1Test

```java
package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV1Test {

    @Test
    public void begin_end() throws Exception {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.end(status);
    }

    @Test
    public void begin_exception() throws Exception {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.exception(status, new IllegalArgumentException());
    }
}
```

테스트 코드를 보면 로그 추적기를 어떻게 실행해야 하는지, 그리고 어떻게 동작하는지 이해가 될 것이다.

#### begin_end() - 실행 로그

```text
[41bbb3b7] hello
[41bbb3b7] hello time=5ms
```

#### begin_exception() - 실행 로그

```text
[898a3def] hello
[898a3def] hello time=13ms ex=java.lang.IllegalStateException
```

