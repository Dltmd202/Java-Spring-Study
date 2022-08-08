# 스프링 AOP - 포인트컷

## 포인트컷 지시자

애스펙트J는 포인트컷을 편리하게 표현하기 위한 특별한 표현식을 제공한다.

* 예) `@Pointcut("execution(* hello.aop.order..*(..))")`


* 포인트컷 표현식은 AspectJ pointcut expression 즉 애스펙트J가 제공하는 포인트컷 표현식을 줄여서 말하는 것이다.

#### 포인트컷 지시자
포인트컷 표현식은 `execution` 같은 포인트컷 지시자(Pointcut Designator)로 시작한다. 줄여서 PCD라 한다.


#### 포인트컷 지시자의 종류 종류
* `execution` : 메소드 실행 조인 포인트를 매칭한다. 스프링 AOP에서 가장 많이 사용하고, 기능도 복잡하다.
* `within` : 특정 타입 내의 조인 포인트를 매칭한다.
* `args` : 인자가 주어진 타입의 인스턴스인 조인 포인트
* `this` : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
* `target` : Target 객체(스프링 AOP 프록시가 가르키는 실제 대상)를 대상으로 하는 조인 포인트 
* `@target` : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
* `@within` : 주어진 애노테이션이 있는 타입 내 조인 포인트
* `@annotation` : 메서드가 주어진 애노테이션을 가지고 있는 조인 포인트를 매칭
* `@args` : 전달된 실제 인수의 런타임 타입이 주어진 타입의 애노테이션을 갖는 조인 포인트 
* `bean` : 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정한다.


## 예제 만들기

#### ClassAop

```java
package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {
}
```

#### MethodAop

```java
package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAop {

    String value();
}
```

#### MemberService

```java
package hello.aop.member;

public interface MemberService {
    String hello(String param);
}
```


#### MemberServiceImpl

```java
package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService{

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param){
        return "ok";
    }
}
```

#### ExecutionTest

```java
package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod(){
        log.info("helloMethod={}", helloMethod);
    }
}
```

`AspectJExpressionPoincut`이 포인트컷 표현식을 처리해주는 클래스이다. 여기서 포인트컷 표현식을 지정하면 된다.
`AspectJExpressionPointcut`은 상위에 `Pointcut` 인터페이스를 가진다.

`printMethod()` 테스트는 `MemberServiceImpl.hello(String)` 메서드 정보를 출력해준다.

## execution1


#### execution 문법

```text
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-
pattern(param-pattern)
      throws-pattern?)
      
execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
```


* 메소드 실행 조인 포인트를 매칭한다.
* ?는 생략할 수 있다.
* `*`같은 패턴을 지정할 수 있다.

#### 가장 정확한 포인트컷
먼저 `MemberServiceImpl.hello(String)` 메서드와 가장 정확하게 모든 내용이 매칭되는 표현식이다.


#### ExcutionTest - 추가

```java
@Test
void exactMatch() {
    pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String)");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```

* `AspectJExpressionPointcut` 에 `pointcut.setExpression` 을 통해서 포인트컷 표현식을 적용할 수 있다. 
* `pointcut.matches(메서드,대상 클래스)`를 실행하면 지정한 포인트컷 표현식의 매칭 여부를 `true`, `false` 로 반환한다.


#### 매칭 조건
* 접근제어자?: `public`
* 반환타입: `String`
* 선언타입?: `hello.aop.member.MemberServiceImpl` 
* 메서드이름: `hello`
* 파라미터: `(String)`
* 예외?: 생략


#### 가장 많이 생략한 포인트컷

```java
@Test
void allMatch() {
    pointcut.setExpression("execution(* *(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```

#### 매칭 조건
* 접근제어자?: 생략 
* 반환타입: `*` 
* 선언타입?: 생략 
* 메서드이름: `*` 
* 파라미터: `(..)`
* 예외?: 없음


* `*` 은 아무 값이 들어와도 된다는 뜻이다. 
* 파라미터에서 `..`은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다. 

#### 메서드 이름 매칭 관련 포인트컷

```java
@Test
void nameMatch() {
    pointcut.setExpression("execution(* hello(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void nameMatchStar1() {
    pointcut.setExpression("execution(* hel*(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void nameMatchStar2() {
    pointcut.setExpression("execution(* *el*(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}


@Test
void nameMatchFalse() {
    pointcut.setExpression("execution(* nono(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}
```

메서드 이름 앞 뒤에 `*`을 사용해서 매칭할 수 있다.

#### 패키지 매핑 관련 포인트컷

`hello.aop.member.*(1).*(2)`

* (1): 타입
* (2): 메서드 이름

#### 패키지에서 `.`, `..` 의 차이를 이해해야 한다.

* `.` : 정확하게 해당 위치의 패키지
* `..` : 해당 위치의 패키지와 그 하위 패키지도 포함


## execution2

#### 타입 매칭 - 부모 타입 허용

```java
@Test
void typeExactMatch() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void typeMatchSuperType() {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```

`typeExactMatch()` 는 타입 정보가 정확하게 일치하기 때문에 매칭된다.
`typeMatchSuperType()` 을 주의해서 보아야 한다.
`execution` 에서는 `MemberService` 처럼 부모 타입을 선언해도 그 자식 타입은 매칭된다. 
다형성에서 `부모타입 = 자식타입` 이 할당 가능하다는 점을 떠올려보면 된다.


#### 타입 매칭 - 부모 타입에 있는 메서드만 허용

```java
@Test
void typeMatchInternal() throws NoSuchMethodException{
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException{
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
}
```

`typeMatchInternal()` 의 경우 `MemberServiceImpl` 를 표현식에 선언했기 때문에 그 안에 있는 `internal(String)` 메서드도 
매칭 대상이 된다.
`typeMatchNoSuperTypeMethodFalse()` 를 주의해서 보아야 한다.
이 경우 표현식에 부모 타입인 `MemberService` 를 선언했다. 
그런데 자식 타입인 `MemberServiceImpl` 의 `internal(String)` 메서드를 매칭하려 한다. 
이 경우 매칭에 실패한다. `MemberService` 에는 `internal(String)` 메서드가 없다!
부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다. 
그래서 부모 타입에 있는 `hello(String)` 메서드는 매칭에 성공하지만, 부모 타입에 없는 `internal(String)` 는 매칭에 실패한다.


#### 파라미터 매칭

```java
// String 타입의 파라미터 허용
@Test
void argsMatch() {
    pointcut.setExpression("execution(* *(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

// 파리미터가 없어야 함
@Test
void argsMatchNoArgs(){
    pointcut.setExpression("execution(* *())");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}

//정확히 하나의 파라미터 허용, 모든 타입 허용
@Test
void argsMatchStar() {
    pointcut.setExpression("execution(* *(*))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//숫자와 무관하게 모든 파라미터, 모든 타입 허용
// 파라미터가 없어도 됨
@Test
void argsMatchAll() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
@Test
void argsMatchComplex() {
    pointcut.setExpression("execution(* *(String, ..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```

#### execution 파라미터 매칭 규칙은 다음과 같다.
* `(String)` : 정확하게 String 타입 파라미터
* `()` : 파라미터가 없어야 한다.
* `(*)` : 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
* `(*, *)` : 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
* `(..)` : 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 참고로 파라미터가 없어도 된다. `0..*` 로 이해하면 된다.
* `(String, ..)` : String 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
  * 예) `(String)` , `(String, Xxx)` , `(String, Xxx, Xxx)` 허용
                                                                                
## within

within 지시자는 특정 타입 내의 조인 포인트에 대한 매칭을 제한한다. 
쉽게 이야기해서 해당 타입이 매칭되면 그 안의 메서드(조인 포인트)들이 자동으로 매칭된다.
문법은 단순한데 `execution` 에서 타입 부분만 사용한다고 보면 된다.

#### WithInTest

```java
package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class WithinTest {

  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException{
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  @Test
  void withinExact() {
    pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void withinStar() {
    pointcut.setExpression("within(hello.aop.member.*Service*)");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void withinSubPackage() {
    pointcut.setExpression("within(hello.aop..*)");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }
}
```

> 주의
> 
> within 사용시 주의해야 할 점이 있다. 
> 표현식에 부모 타입을 지정하면 안된다는 점이다. 
> 정확하게 타입이 맞아야 한다. 이 부분에서 execution 과 차이가 난다.


#### WithinTest - 추가

```java
@Test
@DisplayName("타겟의 타입에만 직접 적용, 인터페이스를 선정하면 안된다.")
void withSuperTypeFalse(){
    pointcut.setExpression("within(hello.aop.member.MemberService)");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}

@Test
@DisplayName("execution은 타입 기반, 인터페이스를 선정 가능.")
void executionSuperTypeTrue(){
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
