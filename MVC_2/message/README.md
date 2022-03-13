## 메시지, 국제화 소개

#### 메시지
기획자가 화면에 보이는 문구가 마음에 들지 않는다고, 상품명이라는 단어를 모두 상품이름으로 고쳐달라고 하면 어떻게 해야할까

여러 화면에 보이는 상품명, 가격, 수량 등, label 에 있는 단어를 변경하려면 다음 화면들을 다 찾아가면서 모두 변경해야 한다.

지금처럼 화면 수가 적으면 문제가 되지 않지만 화면이 수십개 이상이라면 수십개의 파일을 모두 고쳐야 한다.

* addForm.html , editForm.html , item.html , items.html

왜냐하면 해당 HTML 파일에 메시지가 하드코딩 되어 있기 때문이다.
이런 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.

예를 들어서 `messages.properteis` 라는 메시지 관리용 파일을 만들고

```yaml
item=상품 
item.id=상품 ID 
item.itemName=상품명 
item.price=가격 
item.quantity=수량
```

각 HTML들은 다음과 같이 해당 데이터를 key 값으로 불러서 사용하는 것이다.

#### `addForm.html`

```html
<label for="itemName" th:text="#{item.itemName}"></label>
```

#### `editForm.html`

```html
<label for="itemName" th:text="#{item.itemName}"></label>
```

### 국제화

메시지에서 한 발 더 나가보자.
메시지에서 설명한 메시지 파일(`messages.properteis`)을 각 나라별로 별도로 관리하면 서비스를 국제화 할 수 있다.
예를 들어서 다음과 같이 2개의 파일을 만들어서 분류한다.

#### `messages_en.propertis`

```yaml
item=Item
item.id=Item ID
item.itemName=Item Name
item.price=price
item.quantity=quantity
```

#### messages_ko.propertis

```yaml
item=상품 
item.id=상품 ID 
item.itemName=상품명 
item.price=가격 
item.quantity=수량
```


영어를 사용하는 사람이면 `messages_en.propertis` 를 사용하고,
한국어를 사용하는 사람이면 `messages_ko.propertis` 를 사용하게 개발하면 된다.

이렇게 하면 사이트를 국제화 할 수 있다.
한국에서 접근한 것인지 영어에서 접근한 것인지는 인식하는 방법은 HTTP `accept-language` 해더 값을
사용하거나 사용자가 직접 언어를 선택하도록 하고, 쿠키 등을 사용해서 처리하면 된다.
메시지와 국제화 기능을 직접 구현할 수도 있겠지만, 스프링은 기본적인 메시지와 국제화 기능을 모두 제공한다. 
그리고 타임리프도 스프링이 제공하는 메시지와 국제화 기능을 편리하게 통합해서 제공한다. 

### 스프링 메시지 소스 설정

스프링은 기본적인 메시지 관리 기능을 제공한다.

메시지 관리 기능을 사용하려면 스프링이 제공하는 `MessageSource` 를 스프링 빈으로 등록하면 되는데, 
`MessageSource` 는 인터페이스이다. 따라서 구현체인 `ResourceBundleMessageSource` 를 스프링 빈으로 등록하면 된다.

```java
@Bean
public MessageSource messageSource() {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("messages", "errors");
      messageSource.setDefaultEncoding("utf-8");
      return messageSource;
}
```

* `basenames` : 설정 파일의 이름을 지정한다. 
* `messages` 로 지정하면 `messages.properties` 파일을 읽어서 사용한다.
  * 추가로 국제화 기능을 적용하려면 `messages_en.properties` , `messages_ko.properties` 와 같이 파일명 마지막에 언어 정보를 주면된다. 
  만약 찾을 수 있는 국제화 파일이 없으면 `messages.properties` (언어정보가 없는 파일명)를 기본으로 사용한다. 
  * 파일의 위치는 `/resources/messages.properties` 에 두면 된다. 
  * 여러 파일을 한번에 지정할 수 있다. 여기서는 `messages` , `errors` 둘을 지정했다. 
* `defaultEncoding` : 인코딩 정보를 지정한다. `utf-8` 을 사용하면 된다


### 스프링 부트

스프링 부트를 사용하면 스프링 부트가 `MessageSource` 를 자동으로 스프링 빈으로 등록한다.

### 스프링 부트 메시지 소스 설정

스프링 부트를 사용하면 다음과 같이 메시지 소스를 설정할 수 있다.

#### `application.properties`

```yaml
spring.messages.basename=messages,config.i18n.messages
```

### 스프링 부트 메시지 소스 기본 값

`spring.messages.basename=messages`

`MessageSource` 를 스프링 빈으로 등록하지 않고, 
스프링 부트와 관련된 별도의 설정을 하지 않으면 `messages` 라는 이름으로 기본 등록된다. 
따라서 `messages_en.properties` , `messages_ko.properties` , `messages.properties` 
파일만 등록하면 자동으로 인식된다.


### 스프링 메시지 소스 사용

#### MessageSource 인터페이스

```java
public interface MessageSource {
    String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);
    String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
```

```java
package hello.itemservice.message;
  import org.junit.jupiter.api.Test;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.context.SpringBootTest;
  import org.springframework.context.MessageSource;
  import static org.assertj.core.api.Assertions.*;
  
  @SpringBootTest
  public class MessageSourceTest {
      @Autowired
      MessageSource ms;
      @Test
      void helloMessage() {
        String result = ms.getMessage("hello", null, null); assertThat(result).isEqualTo("안녕");
      } 
  }
```

* `ms.getMessage("hello", null, null)` 
  * code: `hello` 
  * args: `null` 
  * locale: `null`


### MessageSourceTest 추가 - 메시지가 없는 경우, 기본 메시지

```java
package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    @Autowired MessageSource ms;

    @Test
    public void helloMessage() throws Exception{
        String result = ms.getMessage("hello", null, null);
        System.out.println(result);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    public void notFoundMessageCode() throws Exception{
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    public void notFoundMessageCodeDefualtMessage() throws Exception{
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

}
```

* 메시지가 없는 경우에는 `NoSuchMessageException` 이 발생한다.
* 메시지가 없어도 기본 메시지( `defaultMessage` )를 사용하면 기본 메시지가 반환된다.


### MessageSourceTest - 매개변수 사용

```java
@Test
void argumentMessage() {
    String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null); 
    assertThat(result).isEqualTo("안녕 Spring");
}
```

다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다. 
`hello.name=안녕 {0}` Spring 단어를 매개변수로 전달 `안녕 Spring`


### 국제화 파일 선택

locale 정보를 기반으로 국제화 파일을 선택한다. 
* Locale이 `en_US` 의 경우 `messages_en_US` ->  `messages_en` ->  `messages` 순서로 찾는다. 
* Locale 에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다고 이해하면 된다.

#### MessageSourceTest - 국제화 파일 선택1

```java
@Test
void defaultLang(){
    assertThat(ms.getMessage("hello",null,null)).isEqualTo("안녕");
    assertThat(ms.getMessage("hello",null,Locale.KOREA)).isEqualTo("안녕");
}
```

* `ms.getMessage("hello", null, null)` : locale 정보가 없으므로 messages 를 사용 
* `ms.getMessage("hello", null, Locale.KOREA)` : locale 정보가 있지만, `message_ko` 
  가 없으므로 `messages` 를 사용

#### MessageSourceTest - 국제화 파일 선택2

```java
@Test
void enLang() {
    assertThat(ms.getMessage("hello", null,Locale.ENGLISH)).isEqualTo("hello");
}
```

* `ms.getMessage("hello", null, Locale.ENGLISH)` : locale 정보가 Locale.ENGLISH 
  이므로 `messages_en` 을 찾아서 사용


### 웹 애플리케이션에 메시지 적용하기

#### 타임리프 메시지 적용

타임리프의 메시지 표현식 `#{...}` 를 사용하면 스프링의 메시지를 편리하게 조회할 수 있다. 
예를 들어서 방금 등록한 상품이라는 이름을 조회하려면 `#{label.item}` 이라고 하면 된다.

#### 렌더링 전
```html
  <div th:text="#{label.item}"></h2>
```

#### 렌더링 후
```html
<div>상품</h2>
```

#### 파라미터
`hello.name=안녕 {0}`

`<p th:text="#{hello.name(${item.itemName})}"></p>`


#### 웹 애플리케이션에 국제화 적용하기

```yaml
label.item=Item
label.item.id=Item ID
label.item.itemName=Item Name
label.item.price=price
label.item.quantity=quantity
page.items=Item List
page.item=Item Detail
```

이것으로 국제화 작업은 거의 끝났다. 앞에서 템플릿 파일에는 모두 #{...} 를 통해서 메시지를 사용하도록 적용해두었기 때문이다.

#### 웹으로 확인하기

웹 브라우저의 언어 설정 값을 변경하면서 국제화 적용을 확인해보자.
크롬 브라우저 설정 언어를 검색하고, 우선 순위를 변경하면 된다.
우선순위를 영어로 변경하고 테스트해보자.
웹 브라우저의 언어 설정 값을 변경하면 요청시 `Accept-Language` 의 값이 변경된다.

`Accept-Language` 는 클라이언트가 서버에 기대하는 언어 정보를 담아서 요청하는 HTTP 요청 헤더이다.

### 스프링의 국제화 메시지 선택

앞서 `MessageSource` 테스트에서 보았듯이 메시지 기능은 `Locale` 정보를 알아야 언어를 선택할 수 있다. 

결국 스프링도 `Locale` 정보를 알아야 언어를 선택할 수 있는데, 
스프링은 언어 선택시 기본으로 `Accept-Language` 헤더의 값을 사용한다.

#### LocaleResolver 인터페이스

```java
public interface LocaleResolver {
  Locale resolveLocale(HttpServletRequest request);

  void setLocale(HttpServletRequest request,
                 @Nullable HttpServletResponse response, @Nullable Locale locale);
}
```

#### LocaleResolver 변경

만약 `Locale` 선택 방식을 변경하려면 `LocaleResolver` 의 구현체를 변경해서 쿠키나 세션 기반의 `Locale` 선택 기능을 사용할 수 있다. 
예를 들어서 고객이 직접 `Locale` 을 선택하도록 하는 것이다.