# 스프링 핵심 원리 이해2 - 객체 지향 원리 적용

## 새로운 할인 정책 개발

#### RateDiscountPolicy 추가

![](res/img.png)

#### [RateDiscountPolicy 코드 추가](./src/main/java/hello/core/discount/RateDiscountPolicy.java)

```java
package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicy implements DiscountPolicy{
    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
```

#### 테스트 작성

```java
package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {
    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o() {
        Member member = new Member(1L, "memberVIP", Grade.VIP);
        int discount = discountPolicy.discount(member, 10000);
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x() {
        Member member = new Member(2L, "memberBASIC", Grade.BASIC);
        int discount = discountPolicy.discount(member, 1000);
        assertThat(discount).isEqualTo(1000);
    }
}
```

## 새로운 할인 정책 적용과 문제점

#### 비율 할인 정책을 적용


할인 정책을 변경하려면 클라이언트인 `OrderServiceImpl` 코드를 고쳐야 한다.

```java
public class OrderServiceImpl implements OrderService { 
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
}
```


#### 문제점

* 역할과 구현을 충실하게 분리했다.
* 다형성도 활용하고, 인터페이스와 구현 객체를 분리했다.
* OCP, DIP 같은 객체지향 설계 원칙을 준수했다.
  * 그렇지 않다.
* DIP: 주문서비스 클라이언트(`OrderServiceImpl`)는 `DiscountPolicy` 인터페이스에 의존하면서 DIP를 지킨것인가
  * 클래스 의존관계를 분석하면 추상뿐만 아니라 구체 클래스에도 의존하고 있다.
    * 추상 의존: `DiscountPolicy`
    * 구체 클래스: `FixDiscountPolicy`, `RateDiscountPolicy'
* OCP: 변경하지 않고 확장해야 한다,
  * 현재 코드는 기능을 확장해서 변경하면, 클라이언트 코드에 영향을 준다. 따라서 OCP를 위반했다.

#### 왜 클라이언트 코드를 변경해야 하는가?

#### 기대했던 의존관계

![](res/img_1.png)

지금까지 단순히 `DiscountPolicy` 인터페이스만 의존한다고 생각했다.


#### 실제 의존관계

![](res/img_2.png)

잘보면 클라이언트인 `OrderServiceImpl` 이 `DiscountPolicy` 인터페이스 뿐만 아니라 
`FixDiscountPolicy` 인 구체 클래스도 함께 의존하고 있다. 실제 코드를 보면 의존하고 있다 


DIP 위반

#### 정책 변경

![](res/img_3.png)

> 중요!
> 
> `FixDiscountPolicy` 를 `RateDiscountPolicy` 로 변경하는 순간 `OrderServiceImpl` 의 소스 코드도 함께 변경해야 한다
> 
> OCP 위반


### 어떻게 문제를 해결하는 방법

* 클라이언트 코드인 `OrderServiceImpl` 은 `DiscountPolicy` 의 인터페이스 뿐만 아니라 구체 클래스도 함께 의존한다. 
* 그래서 구체 클래스를 변경할 때 클라이언트 코드도 함께 변경해야 한다. 
* DIP 위반 -> 추상에만 의존하도록 변경(인터페이스에만 의존)
* DIP를 위반하지 않도록 인터페이스에만 의존하도록 의존관계를 변경하면 된다.

### 인터페이스에만 의존하도록 설계를 변경

![](res/img_4.png)

#### 인터페이스에만 의존하도록 코드 변경

```java
public class OrderServiceImpl implements OrderService {
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    private DiscountPolicy discountPolicy;
}
```


* 인터페이스에만 의존하도록 설계와 코드를 변경했다. 
* 구현체가 없는데 코드를 실행하는 방법은? 
* 실제 실행을 해보면 NPE(null pointer exception)가 발생한다.


#### 해결방안

이 문제를 해결하려면 누군가가 클라이언트인 `OrderServiceImpl` 에 `DiscountPolicy` 의
구현 객체를 대신 생성하고 주입해주어야 한다.
