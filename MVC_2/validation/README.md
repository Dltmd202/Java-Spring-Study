## 검증 직접 처리

#### 상품 저장 성공

![](./res/1.png)

사용자가 상품 등록 폼에서 정상 범위의 데이터를 입력하면, 서버에서는 검증 로직이 통과하고, 상품을 저장하고, 
상품 상세 화면으로 redirect한다.

#### 상품 저장 실패

![](./res/2.png)

고객이 상품 등록 폼에서 상품명을 입력하지 않거나, 가격, 수량 등이 너무 작거나 커서 검증 범위를 넘어서면, 서버 검증 로직이 실패해야 한다. 
이렇게 검증에 실패한 경우 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값을 잘못 입력했는지 친절하게 알려주어야 한다.

#### 검증 오류 보관

`Map<String, String> errors = new HashMap<>();`

만약 검증시 오류가 발생하면 어떤 검증에서 오류가 발생했는지 정보를 담아둔다.

#### 검증 로직


```java
if (!StringUtils.hasText(item.getItemName())) { 
    errors.put("itemName", "상품 이름은 필수입니다.");
}
```

특정 필드를 넘어서는 오류를 처리해야 할 수도 있다. 이때는 필드 이름을 넣을 수 없으므로 `globalError` 라는 key 를 사용한다.


#### 글로벌 오류 메시지
```html
<div th:if="${errors?.containsKey('globalError')}">
    <p class="field-error" th:text="${errors['globalError']}">전체 오류 메시지</p>
</div>
```

> 참고
> 
> 만약 여기에서 `errors` 가 `null` 이라면 어떻게 될까?
> 생각해보면 등록폼에 진입한 시점에는 `errors` 가 없다.
> 따라서 `errors.containsKey()` 를 호출하는 순간 `NullPointerException` 이 발생한다.
> 
> `errors?.` 은 `errors` 가 `null` 일때 `NullPointerException` 이 발생하는 대신, `null` 을 반환하는 문법이다.
> `th:if` 에서 `null` 은 실패로 처리되므로 오류 메시지가 출력되지 않는다
> 
> 이것은 스프링의 SpringEL이 제공하는 문법이다. 자세한 내용은 다음을 참고하자.
> https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions-operator-safe-navigation

#### 남은 문제점

* 뷰 템플릿에서 중복 처리가 많다. 뭔가 비슷하다.

* 타입 오류 처리가 안된다. `Item` 의 `price` , `quantity` 같은 숫자 필드는 
  타입이 `Integer` 이므로 문자 타입으로 설정하는 것이 불가능하다.
  숫자 타입에 문자가 들어오면 오류가 발생한다. 
  그런데 이러한 오류는 스프링MVC에서 컨트롤러에 진입하기도 전에 예외가 발생하기 때문에, 
  컨트롤러가 호출되지도 않고, 400 예외가 발생하면서 오류 페이지를 띄워준다.

* `Item`의 `price`에 문자를 입력하는 것 처럼 타입 오류가 발생해도 고객이 입력한 문자를 화면에 남겨야 한다. 
  만약 컨트롤러가 호출된다고 가정해도 `Item` 의 `price` 는 `Integer` 이므로 문자를 보관할 수가 없다. 
  결국 문자는 바인딩이 불가능하므로 고객이 입력한 문자가 사라지게 되고, 고객은 본인이 어떤 내용을 입력해서 오류가 발생했는지 이해하기 어렵다.
  결국 고객이 입력한 값도 어딘가에 별도로 관리가 되어야 한다.
