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

### BindingResult

#### ValidationItemControllerV2 - addItemV1

```java
@PostMapping("/add")
public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,
                      RedirectAttributes redirectAttributes, Model model) {
    
    Map<String, String> errors = new HashMap<>();

    if(!StringUtils.hasText(item.getItemName())) {
        errors.put("itemName", "상품 이름은 필수입니다.");
        bindingResult.addError(
                new FieldError("item", "itemName", "상품 이름은 필수입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
        errors.put("price", "가격은 1000 ~ 1,000,000 까지 허용합니다.");
        bindingResult.addError(
                new FieldError("item", "price", "가격은 1000 ~ 1,000,000 까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999){
        errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        bindingResult.addError(
                new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    }

    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if(resultPrice < 10000){
            errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            bindingResult.addError(
                    new ObjectError("item",
                            "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
        }
    }

    if(bindingResult.hasErrors()){
        log.info("errors = {}", bindingResult);
        return "validation/v2/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}
```

#### 주의

`BindingResult bindingResult` 파라미터 위치는 `@ModelAttribute Item item` 다음에 와야 한다.

#### 필드 오류 - FieldError

```java
public FieldError(String objectName, String field, String defaultMessage) {}
```

필드에 오류가 있으면 `FieldError` 객체를 생성해서 `bindingResult` 에 담아두면 된다. 
* `objectName` : `@ModelAttribute` 이름 
* `field` : 오류가 발생한 필드 이름 
* `defaultMessage` : 오류 기본 메시지

#### 글로벌 오류 - ObjectError

```java
public ObjectError(String objectName, String defaultMessage) {}
```

특정 필드를 넘어서는 오류가 있으면 `ObjectError` 객체를 생성해서 `bindingResult` 에 담아두면 된다. 
* `objectName` : `@ModelAttribute` 의 이름
* `defaultMessage` : 오류 기본 메시지

### BindingResult2

* 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다. 
* `BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!

#### 예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?

* `BindingResult` 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로
  이동한다.

* `BindingResult` 가 있으면 오류 정보( `FieldError` )를 `BindingResult` 에 담아서 컨트롤러를
  정상 호출한다.

#### `BindingResult`에 검증 오류를 적용하는 3가지 방법

* `@ModelAttribute` 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 `FieldError` 생성해서
  `BindingResult` 에 넣어준다.
* 개발자가 직접 넣어준다. 
* `Validator` 사용

#### 타입 오류 확인

숫자가 입력되어야 할 곳에 문자를 입력해서 타입을 다르게 해서 `BindingResult` 를 호출하고 `bindingResult` 의 값을 확인해보자.


#### 주의

* `BindingResult` 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 예를 들어서 `@ModelAttribute Item item` , 
  바로 다음에 `BindingResult` 가 와야 한다.
* `BindingResult` 는 `Model`에 자동으로 포함된다.

### `BindingResult`와 `Errors`

* `org.springframework.validation.Errors`
* `org.springframework.validation.BindingResul`

`BindingResult` 는 인터페이스이고, `Errors` 인터페이스를 상속받고 있다.
실제 넘어오는 구현체는 `BeanPropertyBindingResult` 라는 것인데, 둘다 구현하고 있으므로 `BindingResult` 대신에 `Errors` 를 사용해도 된다. 
`Errors` 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. `BindingResult` 는 여기에 더해서 추가적인 기능들을 제공한다. 
`addError()` 도 `BindingResult` 가 제공하므로 여기서는 `BindingResult` 를 사용하자. 주로 관례상 `BindingResult` 를 많이 사용한다.


### `FieldError`, `ObjectError`


```java
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        Map<String, String> errors = new HashMap<>();

        if(!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
            bindingResult.addError(
                    new FieldError(
                            "item",
                            "itemName",
                            item.getItemName(),
                            false,
                            null,
                            null,
                            "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.put("price", "가격은 1000 ~ 1,000,000 까지 허용합니다.");
            bindingResult.addError(
                    new FieldError("item",
                            "price",
                            item.getPrice(),
                            false,
                            null,
                            null,
                            "가격은 1000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
            bindingResult.addError(
                    new FieldError("item",
                            "quantity",
                            item.getQuantity(),
                            false,
                            null,
                            null,
                            "수량은 최대 9,999 까지 허용합니다."));
        }

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
                bindingResult.addError(
                        new ObjectError("item",
                                null,
                                null,
                                "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

#### FieldError 생성자

`FieldError` 는 두 가지 생성자를 제공한다.

```java
public FieldError(String objectName, String field, String defaultMessage);
public FieldError(String objectName, String field, @Nullable Object rejectedValue, 
        boolean bindingFailure, @Nullable String[] codes,@Nullable Object[] arguments, 
        @Nullable String defaultMessage)
```

파라미터 목록

* `objectName` : 오류가 발생한 객체 이름 
* `field` : 오류 필드 
* `rejectedValue` : 사용자가 입력한 값(거절된 값)
* `bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 codes : 메시지 코드 
* `arguments` : 메시지에서 사용하는 인자 
* `defaultMessage` : 기본 오류 메시지

#### 오류 발생시 사용자 입력 값 유지

` new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~1,000,000 까지 허용합니다.")`

사용자의 입력 데이터가 컨트롤러의 `@ModelAttribute` 에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기 어렵다. 
예를 들어서 가격에 숫자가 아닌 문자가 입력된다면 가격은 `Integer` 타입이므로 문자를 보관할 수 있는 방법이 없다. 
그래서 오류가 발생한 경우 사용자 입력 값을 보관하는 별도의 방법이 필요하다. 
그리고 이렇게 보관한 사용자 입력 값을 검증 오류 발생시 화면에 다시 출력하면 된다.
`FieldError` 는 오류 발생시 사용자 입력 값을 **저장**하는 기능을 제공한다.

#### 타임리프의 사용자 입력 값 유지

`th:field="*{price}"`

타임리프의 `th:field` 는 매우 똑똑하게 동작하는데, 정상 상황에는 모델 객체의 값을 사용하지만, 
오류가 발생하면 `FieldError` 에서 보관한 값을 사용해서 값을 출력한다.

#### 스프링의 바인딩 오류 처리

타입 오류로 바인딩에 실패하면 스프링은 `FieldError` 를 생성하면서 사용자가 입력한 값을 넣어둔다. 
그리고 해당 오류를 `BindingResult` 에 담아서 컨트롤러를 호출한다. 
따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.


### 오류 코드와 메시지 처리1

#### FieldError 생성자
```java
public FieldError(String objectName, String field, String defaultMessage);
public FieldError(String objectName, String field, @Nullable Object rejectedValue, 
        boolean bindingFailure, @Nullable String[] codes, 
        @Nullable Object[] arguments, @Nullable String defaultMessage);
```

#### 파라미터 목록

* `objectName` : 오류가 발생한 객체 이름
* `field` : 오류 필드
* `rejectedValue` : 사용자가 입력한 값(거절된 값)
* `bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 
* `codes` : 메시지 코드
* `arguments` : 메시지에서 사용하는 인자
* `defaultMessage` : 기본 오류 메시지

`FieldError` , `ObjectError` 의 생성자는 `errorCode` , `arguments` 를 제공한다. 
이것은 오류 발생시 오류 코드로 메시지를 찾기 위해 사용된다.

```java
new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"}, new Object[]{1000, 1000000};
```

* `codes` : `required.item.itemName` 를 사용해서 메시지 코드를 지정한다. 메시지 코드는 하나가 아니라 배열로 여러 값을 전달할 수 있는데, 
  순서대로 매칭해서 처음 매칭되는 메시지가 사용된다.

* `arguments` : `Object[]{1000, 1000000}` 를 사용해서 코드의 `{0}` , `{1}` 로 치환할 값을 전달한다.


### 오류 코드와 메시지 처리2

컨트롤러에서 `BindingResult` 는 검증해야 할 객체인 `target` 바로 다음에 온다. 
따라서 `BindingResult` 는 이미 본인이 검증해야 할 객체인 `target` 을 알고 있다.

`BindingResult` 가 제공하는 `rejectValue()` , `reject()` 를 사용하면 `FieldError` , `ObjectError` 를 직접 생성하지 않고, 
깔끔하게 검증 오류를 다룰 수 있다.

```java
bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
```

#### `rejectValue()`

```java
void rejectValue(@Nullable String field, String errorCode,
        @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

* `field` : 오류 필드명
* `errorCode` : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)
* `errorArgs` : 오류 메시지에서 `{0}` 을 치환하기 위한 값 
* `defaultMessage` : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

#### 축약된 오류 코드

`FieldError()` 를 직접 다룰 때는 오류 코드를 `range.item.price` 와 같이 모두 입력했다. 그런데
`rejectValue()` 를 사용하고 부터는 오류 코드를 `range` 로 간단하게 입력했다. 그래도 오류 메시지를 잘
찾아서 출력한다. 무언가 규칙이 있는 것 처럼 보인다. 이 부분을 이해하려면 `MessageCodesResolver` 를 이해해야 한다.

### 오류 코드와 메시지 처리3


오류 코드를 만들 때 다음과 같이 자세히 만들 수도 있고, 
`required.item.itemName` : 상품 이름은 필수 입니다. 
`range.item.price` : 상품의 가격 범위 오류 입니다.

또는 다음과 같이 단순하게 만들 수도 있다.

`required` : 필수 값 입니다.
`range` : 범위 오류 입니다.

단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다. 
반대로 너무 자세하게 만들면 범용성이 떨어진다. 가장 좋은 방법은 범용성으로 사용하다가, 
세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.

코드가 있으면 이 메시지를 높은 우선순위로 사용하는 것이다.

```properties
#Level1
required.item.itemName: 상품 이름은 필수 입니다.

#Level2
required: 필수 값 입니다.
```


물론 이렇게 객체명과 필드명을 조합한 메시지가 있는지 우선 확인하고, 
없으면 좀 더 범용적인 메시지를 선택하도록 추가 개발을 해야겠지만, 범용성 있게 잘 개발해두면, 
메시지의 추가 만으로 매우 편리하게 오류 메시지를 관리할 수 있을 것이다.

스프링은 `MessageCodesResolver` 라는 것으로 이러한 기능을 지원한다.


### 오류 코드와 메시지 처리4

#### MessageCodesResolver

* 검증 오류 코드로 메시지 코드들을 생성한다.
* `MessageCodesResolver` 인터페이스이고 `DefaultMessageCodesResolver` 는 기본 구현체이다.
* 주로 다음과 함께 사용 `ObjectError` , `FieldError`

####  DefaultMessageCodesResolver의 기본 메시지 생성 규칙

객체 오류

```text
객체 오류의 경우 다음 순서로 2가지 생성 
1.: code + "." + object name 
2.: code

예) 오류 코드: required, object name: item 
1.: required.item
2.: required
```

필드 오류

```text
필드 오류의 경우 다음 순서로4가지 메시지 코드 생성
1.: code + "." + object name + "." + field
2.: code + "." + field
3.: code + "." + field type
4.: code


예) 오류 코드: typeMismatch, object name "user", field "age", field type: int 
1. "typeMismatch.user.age"
2. "typeMismatch.age"
3. "typeMismatch.int"
4. "typeMismatch"
```

#### 동작 방식
* `rejectValue()` , `reject()` 는 내부에서 `MessageCodesResolver` 를 사용한다. 여기에서 메시지 코드들을 생성한다.
* `FieldError` , `ObjectError` 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다. `MessageCodesResolver` 를 통해서
  생성된 순서대로 오류 코드를 보관한다.
* 이 부분을 `BindingResult` 의 로그를 통해서 확인해보자.
  * `codes [range.item.price, range.price, range.java.lang.Integer, range]`

### 오류 코드와 메시지 처리5

#### 오류 코드 관리 전략

#### 핵심은 구체적인 것에서! 덜 구체적인 것으로!

`MessageCodesResolver` 는 `required.item.itemName` 처럼 구체적인 것을 먼저 만들어주고, 
`required` 처럼 덜 구체적인 것을 가장 나중에 만든다.
이렇게 하면 앞서 말한 것 처럼 메시지와 관련된 공통 전략을 편리하게 도입할 수 있다.

#### 왜 이렇게 복잡하게 사용하는가?

모든 오류 코드에 대해서 메시지를 각각 다 정의하면 개발자 입장에서 관리하기 너무 힘들다.
크게 중요하지 않은 메시지는 범용성 있는 `requried` 같은 메시지로 끝내고, 
정말 중요한 메시지는 꼭 필요할 때 구체적으로 적어서 사용하는 방식이 더 효과적이다.

```properties
#required.item.itemName=상품 이름은 필수입니다.
# range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
# max.item.quantity=수량은 최대 {0} 까지 허용합니다.
# totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}



#==ObjectError==

#Level1
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#Level2 - 생략
totalPriceMin=전체 가격은 {0}원 이상이어야 합니다. 현재 값 = {1}

#==FieldError==
#Level1
required.item.itemName=상품 이름은 필수입니다. range.item.price=가격은 {0} ~ {1} 까지 허용합니다. max.item.quantity=수량은 최대 {0} 까지 허용합니다.

#Level2 - 생략

#Level3
required.java.lang.String = 필수 문자입니다.
required.java.lang.Integer = 필수 숫자입니다.
min.java.lang.String = {0} 이상의 문자를 입력해주세요.
min.java.lang.Integer = {0} 이상의 숫자를 입력해주세요.
range.java.lang.String = {0} ~ {1} 까지의 문자를 입력해주세요.
range.java.lang.Integer = {0} ~ {1} 까지의 숫자를 입력해주세요.
max.java.lang.String = {0} 까지의 문자를 허용합니다.
max.java.lang.Integer = {0} 까지의 숫자를 허용합니다.

#Level4
required = 필수 값 입니다.
min= {0} 이상이어야 합니다.
range= {0} ~ {1} 범위를 허용합니다. max= {0} 까지 허용합니다.
```

크게 객체 오류와 필드 오류를 나누었다. 그리고 범용성에 따라 레벨을 나누어두었다.

`itemName` 의 경우 `required` 검증 오류 메시지가 발생하면 다음 코드 순서대로 메시지가 생성된다.

1. `required.item.itemName`
2. `required.itemName`
3. `required.java.lang.String`
4. `required`


### `ValidationUtils`


#### `ValidationUtils` 사용 전

```java
if (!StringUtils.hasText(item.getItemName())) { bindingResult.rejectValue("itemName", "required", "기본: 상품 이름은
필수입니다."); }
```

#### `ValidationUtils` 사용 후

```java
ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName","required");
```

### 오류 코드와 메시지 처리6

#### 스프링이 직접 만든 오류 메시지 처리

검증 오류 코드는 다음과 같이 2가지로 나눌 수 있다.


* 개발자가 직접 설정한 오류 코드 `rejectValue()` 를 직접 호출 
* 스프링이 직접 검증 오류에 추가한 경우(주로 타입 정보가 맞지 않음)

price 필드에 문자 "A"를 입력해보자.

로그를 확인해보면 `BindingResult` 에 `FieldError` 가 담겨있고, 다음과 같은 메시지 코드들이 생성된 것을 확인할 수 있다.


#### 다음과 같이 4가지 메시지 코드가 입력되어 있다.

* typeMismatch.item.price 
* typeMismatch.price 
* typeMismatch.java.lang.Integer 
* typeMismatch


스프링은 타입 오류가 발생하면 `typeMismatch` 라는 오류 코드를 사용한다. 
이 오류 코드가 `MessageCodesResolver` 를 통하면서 4가지 메시지 코드가 생성된 것이다.


아직 `errors.properties` 에 메시지 코드가 없기 때문에 스프링이 생성한 기본 메시지가 출력된다.

`Failed to convert property value of type java.lang.String to required type
java.lang.Integer for property price; nested exception is
java.lang.NumberFormatException: For input string: "A"`


#### `error.properties` 추가

```propertiese
#추가
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
typeMismatch=타입 오류입니다.
```

### Validator 분리1

#### 목표
복잡한 검증 로직을 별도로 분리


컨트롤러에서 검증 로직이 차지하는 부분은 매우 크다. 이런 경우 별도의 클래스로 역할을 분리하는 것이
좋다. 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다.

#### `ItemValidator`

```java
package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return Item.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Item item = (Item) target;


    if(!StringUtils.hasText(item.getItemName())) {
      errors.rejectValue("itemName", "required");
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
      errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999){
      errors.rejectValue("quantity", "max", new Object[]{9999}, null);
    }
    if (item.getPrice() != null && item.getQuantity() != null) {
      int resultPrice = item.getPrice() * item.getQuantity();
      if(resultPrice < 10000){
        errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
      }
    }
  }
}
```

#### 스프링은 검증을 체계적으로 제공하기 위해 다음 인터페이스를 제공한다.

```java
public interface Validator {
    boolean supports(Class<?> clazz);
    void validate(Object target, Errors errors);
}
```

* `supports()` {} : 해당 검증기를 지원하는 여부 확인(뒤에서 설명) 
* `validate(Object target, Errors errors)` : 검증 대상 객체와 BindingResult


### Validator 분리2

스프링이 `Validator` 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서다. 
그런데 앞에서는 검증기를 직접 불러서 사용했고, 이렇게 사용해도 된다. 
그런데 `Validator` 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.

#### `WebDataBinder`를 통해서 사용하기

`WebDataBinder` 는 스프링의 파라미터 바인딩의 역할을 해주고 검증 기능도 내부에 포함한다.

```java
    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }
```


이렇게 `WebDataBinder` 에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다. 
`@InitBinder` 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다.

#### @Validated 적용

```java
@PostMapping("/add")
public String addItemV6(@Validated @ModelAttribute Item item,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {

    if(bindingResult.hasErrors()){
        log.info("errors = {}", bindingResult);
        return "validation/v2/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}
```

#### 동작 방식

`@Validated` 는 검증기를 실행하라는 애노테이션이다.
이 애노테이션이 붙으면 앞서 `WebDataBinder` 에 등록한 검증기를 찾아서 실행한다. 
그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 `supports()` 가 사용된다.
여기서는 `supports(Item.class)` 호출되고, 결과가 `true` 이므로 `ItemValidator` 의 `validate()` 가 호출된다.


#### 글로벌 설정 - 모든 컨트롤러에 다 적용

```java
@SpringBootApplication
  public class ItemServiceApplication implements WebMvcConfigurer {
      public static void main(String[] args) {
          SpringApplication.run(ItemServiceApplication.class, args);
}
      @Override
      public Validator getValidator() {
          return new ItemValidator();
      }
}
```

> 주의
> 
> 글로벌 설정을 하면 다음에 설명할 `BeanValidator`가 자동 등록되지 않는다. 
> 글로벌 설정 부분은 주석처리 해두자. 참고로 글로벌 설정을 직접 사용하는 경우는 드물다.


> 참고
> 
> 검증시 `@Validated` `@Valid` 둘다 사용가능하다.
> `javax.validation.@Valid` 를 사용하려면 `build.gradle` 의존관계 추가가 필요하다.
> `implementation 'org.springframework.boot:spring-boot-starter-validation'` > `@Validated` 는 스프링 전용 검증 애노테이션이고, 
> `@Valid` 는 자바 표준 검증 애노테이션이다.


## Bean Validation

### Bean Validation 의존관계 추가

#### `build.gradle`

```java
implementation 'org.springframework.boot:spring-boot-starter-validation'
```


#### `Item`

```java
package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```

#### 검증 애노테이션

`@NotBlank` : 빈값 + 공백만 있는 경우를 허용하지 않는다.
`@NotNull` : null 을 허용하지 않는다.
`@Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다. 
`@Max(9999)` : 최대 9999까지만 허용한다.

> 참고
> 
> `javax.validation.constraints.NotNull`
> `org.hibernate.validator.constraints.Range`
>
> `javax.validation` 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
> `org.hibernate.validator` 로 시작하면 하이버네이트 `validator` 구현체를 사용할 때만 제공되는 검증 기능이다. 

#### 검증기 생성

```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();
```


#### 검증 실행

검증 대상(`item`)을 직접 검증기에 넣고 그 결과를 받는다. `Set` 에는 `ConstraintViolation` 이라는 검증 오류가 담긴다. 
따라서 결과가 비어있으면 검증 오류가 없는 것이다.

```java
Set<ConstraintViolation<Item>> violations = validator.validate(item);
```


#### 실행결과

```java
violation = ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.NotBlank.message}'}
violation.getMessage() = 공백일 수 없습니다
violation = ConstraintViolationImpl{interpolatedMessage='9999 이하여야 합니다', propertyPath=quantity, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.Max.message}'}
violation.getMessage() = 9999 이하여야 합니다
violation = ConstraintViolationImpl{interpolatedMessage='1000에서 1000000 사이여야 합니다', propertyPath=price, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{org.hibernate.validator.constraints.Range.message}'}
violation.getMessage() = 1000에서 1000000 사이여야 합니다
```

#### 스프링 MVC의 Bean Validator

스프링 부트가 `spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 
Bean Validator를 인지하고 스프링에 통합한다.

#### 스프링 부트는 자동으로 글로벌 Validator로 등록한다.

`LocalValidatorFactoryBean` 을 글로벌 `Validator`로 등록한다. 이 Validator는 `@NotNull` 
같은 애노테이션을 보고 검증을 수행한다. 이렇게 글로벌 Validator가 적용되어 있기 때문에, `@Valid` , `@Validated` 만 적용하면 된다.
검증 오류가 발생하면, `FieldError` , `ObjectError` 를 생성해서 `BindingResult` 에 담아준다.


> 주의!
> 
> 다음과 같이 직접 글로벌 `Validator`를 직접 등록하면 스프링 부트는 Bean Validator를 글로벌 `Validator` 로 등록하지 않는다.
> 따라서 애노테이션 기반의 빈 검증기가 동작하지 않는다. 


> 참고
> 
> 검증시 `@Validated` `@Valid` 둘다 사용가능하다.
> `javax.validation.@Valid` 를 사용하려면 `build.gradle` 의존관계 추가가 필요하다.
> `implementation 'org.springframework.boot:spring-boot-starter-validation'`
> `@Validated` 는 스프링 전용 검증 애노테이션이고, `@Valid` 는 자바 표준 검증 애노테이션이다. 둘중 
> 아무거나 사용해도 동일하게 작동하지만, `@Validated` 는 내부에 `groups` 라는 기능을 포함하고 있다.


#### 검증 순서

1. `@ModelAttribute` 각각의 필드에 타입 변환 시도 
   1. 성공하면 다음으로
   2. 실패하면 `typeMismatch` 로 `FieldError` 추가 
2. Validator 적용


#### 바인딩에 성공한 필드만 Bean Validation 적용

`BeanValidator`는 바인딩에 실패한 필드는 `BeanValidation`을 적용하지 않는다.
생각해보면 타입 변환에 성공해서 바인딩에 성공한 필드여야 `BeanValidation` 적용이 의미 있다. 
(일단 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증도 의미가 있다.)

`@ModelAttribute` -> 각각의 필드 타입 변환시도  -> 변환에 성공한 필드만 `BeanValidation` 적용

### Bean Validation - 에러 코드

Bean Validation을 적용하고 `bindingResult` 에 등록된 검증 오류 코드를 보면
오류 코드가 애노테이션 이름으로 등록된다. 마치 `typeMismatch` 와 유사하다.

`NotBlank` 라는 오류 코드를 기반으로 `MessageCodesResolver` 를 통해 다양한 메시지 코드가 순서대로 생성된다.

#### @NotBlank
* NotBlank.item.itemName 
* NotBlank.itemName 
* NotBlank.java.lang.String 
* NotBlank


#### @Range
* Range.item.price 
* Range.price 
* Range.java.lang.Integer 
* Range

#### 메시지 등록

##### `errors.properties`

```properties
#Bean Validation 추가 
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

`{0}` 은 필드명이고, `{1}` , `{2}` ...은 각 애노테이션 마다 다르다.

#### BeanValidation 메시지 찾는 순서

1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}") 
3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.


### Bean Validation - 오브젝트 오류

Bean Validation에서 특정 필드( `FieldError` )가 아닌 해당 오브젝트 관련 오류( `ObjectError` )는 어떻게 처리는
다음과 같이 `@ScriptAssert()` 를 사용한다.

```java
@Data
@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000",
        message = "금액의 총합이 10000원 넘게 주문해주세요")
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;
    
  ...
}
```

#### 메시지 코드
* ScriptAssert.item
* ScriptAssert

그런데 실제 사용해보면 제약이 많고 복잡하다. 그리고 실무에서는 검증 기능이 해당 객체의 범위를 넘어서는 경우들도 종종 등장하는데, 그런 경우 대응이 어렵다.

따라서 오브젝트 오류(글로벌 오류)의 경우 `@ScriptAssert` 을 억지로 사용하는 것 보다는 다음과 같이 
오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장한다.


### Bean Validation - 수정에 적용

#### `ValidationItemControllerV3` - `edit()`


```java
@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000,
                    resultPrice}, null);
        }
    }

    if (bindingResult.hasErrors()){
        log.info("errors = {}", bindingResult);
        return "validation/v3/editForm";
    }

    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
}
```


* `edit()` : Item 모델 객체에 @Validated 를 추가하자. 
* 검증 오류가 발생하면 editForm 으로 이동하는 코드 추가

### Bean Validation - 한계

데이터를 등록할 때와 수정할 때는 요구사항이 다를 수 있다.


#### 등록시 기존 요구사항
* 타입 검증
  * 가격, 수량에 문자가 들어가면 검증 오류 처리

* 필드 검증
  * 상품명: 필수, 공백X
  * 가격: 1000원 이상, 1백만원 이하
  * 수량: 최대 9999

* 특정 필드의 범위를 넘어서는 검증
  * 가격 * 수량의 합은 10,000원 이상

#### 수정시 요구사항

* 등록시에는 `quantity` 수량을 최대 9999까지 등록할 수 있지만 **수정시에는 수량을 무제한**으로 변경할 수 있다.
* 등록시에는 `id` 에 값이 없어도 되지만, **수정시에는 id 값이 필수**이다.

### Bean Validation - groups

동일한 모델 객체를 등록할 때와 수정할 때 각각 다르게 검증하는 방법

* BeanValidation의 groups 기능을 사용한다.
* Item을 직접 사용하지 않고, ItemSaveForm, ItemUpdateForm 같은 폼 전송을 위한 별도의 모델 객체를 만들어서 사용한다.

#### groups 적용


#### 저장용

```java
package hello.itemservice.domain.item;

public interface SaveCheck {
}
```


#### 수정용

```java
package hello.itemservice.domain.item;

public interface UpdateCheck {
}
```

#### Item - groups 적용

```java


@PostMapping("/add")
public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes, Model model) {

        if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
        bindingResult.reject("totalPriceMin", new Object[]{10000,
        resultPrice}, null);
        }
        }

        if(bindingResult.hasErrors()){
        log.info("errors = {}", bindingResult);
        return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
        }

    @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
```

