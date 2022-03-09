# 타임리프

* 공식 사이트: https://www.thymeleaf.org/
* 공식 메뉴얼 - 기본 기능: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
* 공식 메뉴얼 - 스프링 통합: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html

### 타임리프 특징
* 서버 사이드 HTML 렌더링 (SSR) 
* 네츄럴 템플릿 
* 스프링 통합 지원

### 서버 사이드 HTML 렌더링 (SSR)

타임리프는 백엔드 서버에서 HTML을 동적으로 렌더링 하는 용도로 사용된다

### 네츄럴 템플릿

타임리프는 순수 HTML을 최대한 유지하는 특징이 있다.
타임리프로 작성한 파일은 HTML을 유지하기 때문에 웹 브라우저에서 파일을 직접 열어도 내용을 확인할 수 있고, 
서버를 통해 뷰 템플릿을 거치면 동적으로 변경된 결과를 확인할 수 있다.
JSP를 포함한 다른 뷰 템플릿들은 해당 파일을 열면, 예를 들어서 JSP 파일 자체를 그대로 웹 브라우저에서
열어보면 JSP 소스코드와 HTML이 뒤죽박죽 섞여서 웹 브라우저에서 정상적인 HTML 결과를 확인할 수 없다. 
오직 서버를 통해서 JSP가 렌더링 되고 HTML 응답 결과를 받아야 화면을 확인할 수 있다.
반면에 타임리프로 작성된 파일은 해당 파일을 그대로 웹 브라우저에서 열어도 정상적인 HTML 결과를 확인할 수 있다. 
물론 이 경우 동적으로 결과가 렌더링 되지는 않는다. 하지만 HTML 마크업 결과가 어떻게 되는지 파일만 열어도 바로 확인할 수 있다.
이렇게 순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 네츄럴 템플릿 (natural templates)이라 한다.

### 타임리프 사용 선언


`<html xmlns:th="http://www.thymeleaf.org">`

## 텍스트 - text, utext

타임리프는 기본적으로 HTML 테그의 속성에 기능을 정의해서 동작한다. HTML의 콘텐츠(content)에 데이터를 출력할 때는 
다음과 같이 `th:text` 를 사용하면 된다. `<span th:text="${data}">`

HTML 테그의 속성이 아니라 HTML 콘텐츠 영역안에서 직접 데이터를 출력하고 싶으면 다음과 같이 `[[...]]` 를 사용하면 된다.
컨텐츠 안에서 직접 출력하기 = `[[${data}]]`


### Escape

HTML문서는 <,> 같은 특수문자를 기반으로 정의된다.
따라서 뷰템플릿으로 HTML화면을 생성할 때는 출력하는 데이터에 이러한 특수 문자가 있는 것을 주의해서 사용해야 한다.


#### 변경 전

`"Hello Spring!"`

#### 변경 후

`"Hello <b>Spring!</b>"`

개발자가 의도한 것은 `<b>` 가 있으면 해당 부분을 강조하는 것이 목적이었다. 그런데 `<b>` 테그가 그대로 나온다.
소스보기를 하면 `<` 부분이 `&lt;`로 변경된 것을 확인할 수 있다.

### HTML 엔티티

웹브라우저는 `<`를 HTML테그의 시작으로 인식한다. 따라서 `<`를 테그의 시작이 아니라 문자로 표현할 수 있는 방법이 필요한데,
이것을 HTML 엔티티라 한다. 그리고 이렇게 HTML에서 사용하는 특수 문자를 HTML 엔티티로 변경하는 것을 
**이스케이프(escape)**라 한다. 
그리고 타임리프가 제공하는 `th:text` , `[[...]]` 는 기본적으로 **이스케이스(escape)**를 제공한다.

* `<` ->  `&lt;` 
* `>` ->  `&gt;`
* 기타 수 많은 HTML 엔티티가 있다.

### Unescape

이스케이프 기능을 사용하지 않는 방법

타임리프는 다음 두 기능을 제공한다.

* `th:text` ->  `th:utext`
* `[[...]]` ->  `[(...)]`

## SpringEL 다양한 표현식 사용

### Object
* `user.username` : user의 username을 프로퍼티 접근
* `user['username']` : 위와 같음 user.getUsername() 
* `user.getUsername()` : user의 getUsername() 을 직접 호출

### List
* `users[0].username` : List에서 첫 번째 회원을 찾고 username 프로퍼티 접근 list.get(0).getUsername() 
* `users[0]['username']` : 위와 같음 
* `user.getUsername()` : List 에서 첫 번째 회원을 찾고 메서드 직접 호출

### Map
* `userMap['userA'].username` : Map에서 userA를 찾고, username 프로퍼티 접근 -> map.get("userA").getUsername()
* `userMap['userA']['username']` : 위와 같음 
* `userMap['userA'].getUsername()` : Map에서 userA를 찾고 메서드 직접 호출

### 지역 변수 선언


`th:with` 를 사용하면 지역 변수를 선언해서 사용할 수 있다. 지역 변수는 선언한 테그 안에서만 사용할 수 있다.

```html
<h1>지역 변수 - (th:with)</h1>
<div th:with="first=${users[0]}">
    <p>처음 사람의 이름은 <span th:text="${first.username}"></span></p>
</div>
```

### 기본 객체들

타임리프는 기본 객체들을 제공한다.

* `${#request}`
* `${#response}`
* `${#session}`
* `${#servletContext}`
* `${#locale}`

그런데 `#request` 는 `HttpServletRequest` 객체가 그대로 제공되기 때문에 데이터를 조회하려면 
`request.getParameter("data")` 처럼 불편하게 접근해야 한다.

* HTTP 요청 파라미터 접근: `param` 
  * 예) `${param.paramData}` 

* HTTP 세션 접근: `session` 
  * 예) `${session.sessionData}`

* 스프링 빈 접근: `@` 
  * 예) `${@helloBean.hello('Spring!')}`

### 유틸리티 객체와 날짜

타임리프는 문자, 숫자, 날짜, URI등을 편리하게 다루는 다양한 유틸리티 객체들을 제공한다.

#### 타임리프 유틸리티 객체들
* `#message` : 메시지, 국제화 처리
* `#uris` : URI 이스케이프 지원
* `#dates` : `java.util.Date` 서식 지원 
* `#calendars` : `java.util.Calendar` 서식 지원 
* `#temporals` : 자바8 날짜 서식 지원
* `#numbers` : 숫자 서식 지원
* `#strings` : 문자 관련 편의 기능
* `#objects` : 객체 관련 기능 제공
* `#bools` : boolean 관련 기능 제공
* `#arrays` : 배열 관련 기능 제공
* `#lists` , `#sets` , `#maps` : 컬렉션 관련 기능 제공 
* `#ids` : 아이디 처리 관련 기능 제공, 뒤에서 설명

#### 타임리프 유틸리티 객체

* https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utility- objects

#### 유틸리티 객체 예시

* https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expression- utility-objects


### 자바 8 날짜

타임리프에서 자바8 날짜인 `LocalDate` , `LocalDateTime` , `Instant` 를 사용하려면 추가 라이브러리가 필요하다. 
스프링 부트 타임리프를 사용하면 해당 라이브러리가 자동으로 추가되고 통합된다.

#### 자바8 날짜용 유틸리티 객체

#### 사용 예시

```html
 <span th:text="${#temporals.format(localDateTime, 'yyyy-MM-dd HH:mm:ss')}"></
    span>
```