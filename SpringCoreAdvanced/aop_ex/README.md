# 스프링 AOP 예제

## 예제 기반

* `@Trace` 애노테이션으로 로그 출력하기 
* `@Retry` 애노테이션으로 예외 발생시 재시도 하기

#### ExamRepository

```java
package hello.aop.exam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번 실패
     */
    public String save(String itemId){
        seq++;
        if(seq % 5 == 0){
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
```

#### ExamService

```java
package hello.aop.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public void request(String itemId){
        examRepository.save(itemId);
    }
}
```


#### ExamTest

```java
package hello.aop.exam;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ExamTest {

    @Autowired
    ExamService examService;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i={}", i);
            examService.request("data" + i);
        }
    }
}
```

## 로그 출력 AOP


`@Trace` 가 메서드에 붙어 있으면 호출 정보가 출력되는 편리한 기능이다.

#### @Trace

```java
package hello.aop.exam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}
```

#### TraceAspect

```java
package hello.aop.exam.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TraceAspect {

    @Before("@annotation(hello.aop.exam.annotation.Trace)")
    public void doTrace(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        log.info("[trace] {} args={}", joinPoint.getSignature(), args);
    }
}
```


`@annotation(hello.aop.exam.annotation.Trace)` 포인트컷을 사용해서 `@Trace` 가 붙은 메서드에
어드바이스를 적용한다.

#### ExamService - @Trace 추가

```java
package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    @Trace
    public void request(String itemId){
        examRepository.save(itemId);
    }
}
```


`request()` 에 `@Trace` 를 붙였다. 이제 메서드 호출 정보를 AOP를 사용해서 로그로 남길 수 있다.


#### ExamRepository - @Trace 추가

```java
package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번 실패
     */
    @Trace
    public String save(String itemId){
        seq++;
        if(seq % 5 == 0){
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
```

#### 재시도 AOP


`@Retry` 애노테이션이 있으면 예외가 발생했을 때 다시 시도해서 문제를 복구한다.

#### @Retry

```java
package hello.aop.exam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int value() default 3;
}
```


#### RetryAspect

```java
package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {
    
    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable{
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);
        
        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e){
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
```

* 재시도 하는 애스펙트이다.
* `@annotation(retry)`, `Retry retry` 를 사용해서 어드바이스에 애노테이션을 파라미터로 전달한다. 
* `retry.value()` 를 통해서 애노테이션에 지정한 값을 가져올 수 있다.
* 예외가 발생해서 결과가 정상 반환되지 않으면 `retry.value()` 만큼 재시도한다.


#### ExamRepository - @Retry 추가

```java
package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번 실패
     */
    @Trace
    @Retry(4)
    public String save(String itemId){
        seq++;
        if(seq % 5 == 0){
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
```

`ExamRepository.save()` 메서드에 `@Retry(value = 4)` 를 적용했다. 이 메서드에서 문제가 발생하면 4번 재시도 한다.


#### ExamTest - 추가

```java

```