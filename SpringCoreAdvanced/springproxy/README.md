# 스프링이 지원하는 프록시

## 프록시 팩토리


#### 문제점
* 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를 적용하는 방법이 있어야 한다.
* 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해 JDK 동적 프록시가 제공하는 `InvocationHandler` 와 CGLIB가 제공하는 
  `MethodInterceptor` 를 통해 해서 관리해야 한다.
* 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되어야 한다.


### 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를 적용하는 방법이 있어야 한다.

스프링은 유사한 구체적인 기술들이 있을 때, 그것들을 통합해서 일관성 있게 접근할 수 있고, 더욱 편리하게 사용할 수 있는 추상화된 기술을 제공한다.


스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리(`ProxyFactory`)라는 기능을 제공한다.


이전에는 상황에 따라서 JDK 동적 프록시를 사용하거나 CGLIB를 사용해야 했다면, 
이 프록시 팩토리 하나로 편리하게 동적 프록시를 생성할 수 있다.


프록시 팩토리는 인터페이스가 있으면 JDK 동적 프록시를 사용하고, 구체 클래스만 있다면 CGLIB를 사용한다. 그리고 이 설정을 변경할 수도 있다.

#### 프록시 팩토리

![](res/img.png)


![](res/img_1.png)


### 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해 JDK 동적 프록시가 제공하는 `InvocationHandler` 와 CGLIB가 제공하는 `MethodInterceptor` 를 통해 해서 관리해야 한다.


스프링은 이 문제를 해결하기 위해 부가 기능을 적용할 때 `Advice` 라는 새로운 개념을 도입했다. 개발자는 
`InvocationHandler` 나 `MethodInterceptor` 를 신경쓰지 않고, `Advice` 만 만들면 된다.


결과적으로 `InvocationHandler` 나 `MethodInterceptor` 는 `Advice` 를 호출하게 된다.
프록시 팩토리를 사용하면 `Advice` 를 호출하는 전용 `InvocationHandler` , `MethodInterceptor` 를 내부에서 사용한다.


![](res/img_2.png)


![](res/img_3.png)


### 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되어야 한다.

앞서 특정 메서드 이름의 조건에 맞을 때만 프록시 부가 기능이 적용되는 코드를 직접 만들었다. 스프링은 Pointcut 이라는 개념을 도입해서 이 문제를 일관성 있게 해결한다.


## 프록시 팩토리 - 예제 코드1

#### Advice 만들기

`Advice` 는 프록시에 적용하는 **부가 기능 로직**이다. 이것은 JDK 동적 프록시가 제공하는 `InvocationHandler` 와 CGLIB가 제공하는
`MethodInterceptor` 의 개념과 유사한다. 둘을 개념적으로 추상화 한 것이다. 프록시 팩토리를 사용하면 둘 대신에 `Advice` 를 사용하면 된다.


Advice 를 만드는 방법은 여러가지가 있지만, 기본적인 방법은 다음 인터페이스를 구현하면 된다.


#### MethodInterceptor - 스프링이 제공하는 코드

```java
package org.aopalliance.intercept;

public interface MethodInterceptor extends Interceptor {
  Object invoke(MethodInvocation invocation) throws Throwable;
}
```

* `MethodInvocation invocation`
  * 내부에는 다음 메서드를 호출하는 방법, 현재 프록시 객체 인스턴스, `args` , 메서드 정보 등이 포함되어 있다. 
    기존에 파라미터로 제공되는 부분들이 이 안으로 모두 들어갔다고 생각하면 된다.
* CGLIB의 `MethodInterceptor` 와 이름이 같으므로 패키지 이름에 주의하자
  * 여기서 사용하는 `org.aopalliance.intercept` 패키지는 스프링 AOP 모듈(`spring-top`) 안에 들어있다.
* `MethodInterceptor` 는 `Interceptor` 를 상속하고 `Interceptor` 는 `Advice` 인터페이스를 상속한다.


#### TimeAdvice

```java
package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
```

* `TimeAdvice` 는 앞서 설명한 `MethodInterceptor` 인터페이스를 구현한다. 패키지 이름에 주의 
* `Object result = invocation.proceed()`
  * `invocation.proceed()` 를 호출하면 `target` 클래스를 호출하고 그 결과를 받는다.
  * 그런데 기존에 보았던 코드들과 다르게 `target` 클래스의 정보가 보이지 않는다. `target` 
    클래스의 정보는 `MethodInvocation invocation` 안에 모두 포함되어 있다.
  * 그 이유는 바로 다음에 확인할 수 있는데, 프록시 팩토리로 프록시를 생성하는 단계에서 이미 
    `target` 정보를 파라미터로 전달받기 때문이다.


#### ProxyFactoryTest

```java
package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    public void interfaceProxy() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }
}
```

* `new ProxyFactory(target)` : 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께 넘겨준다. 
  프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다. 만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 
  인터페이스가 없고 구체 클래스만 있다면 CGLIB를 통해서 동적 프록시를 생성한다. 여기서는 `target` 이 `new ServiceImpl()` 의 
  인스턴스이기 때문에 `ServiceInterface` 인터페이스가 있다. 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를 생성한다.
* `proxyFactory.addAdvice(new TimeAdvice())` : 프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다. 
  JDK 동적 프록시가 제공하는 `InvocationHandler` 와 CGLIB가 제공하는 `MethodInterceptor` 의 개념과 유사하다. 
  이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스 (`Advice`)라 한다. 번역하면 조언을 해준다고 생각하면 된다.
* `proxyFactory.getProxy()` : 프록시 객체를 생성하고 그 결과를 받는다.


#### 프록시 팩토리를 통한 프록시 적용 확인
프록시 팩토리로 프록시가 잘 적용되었는지 확인하려면 다음 기능을 사용하면 된다. 
* `AopUtils.isAopProxy(proxy)` : 프록시 팩토리를 통해서 프록시가 생성되면 JDK 동적 프록시나, CGLIB 모두 참이다. 
* `AopUtils.isJdkDynamicProxy(proxy)` : 프록시 팩토리를 통해서 프록시가 생성되고, JDK 동적 프록시인 경우 참
* `AopUtils.isCglibProxy(proxy)` : 프록시 팩토리를 통해서 프록시가 생성되고, CGLIB 동적 프록시인 경우 경우 참

