

## HTTP 요청 데이터 - 개요
HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법

**주로 다음 3가지 방법을 사용**

* **GET** - 쿼리 파라미터
  * url **?username=hello&age=20**
  * 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  * 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
* **POST** - HTML Form
  * content-type: application/x-www-form-urlencoded
  * 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20
  * 예)회원 가입, 상품 주문, HTML Form 사용
* **HTTP** message body에 데이터를 직접 담아서 요청
  * HTTP API에서 주로 사용, JSON, XML, TEXT
  * 데이터 형식은 주로 JSON 사용
  * POST, PUT, PATCH

### HTTP 요청 데이터 - GET 쿼리 파라미터
데이터를 클라이언트 서버로 전송

메시지 바디 없이 URL의 쿼리 파라미터를 사용해서 데이터를 전달

쿼리 파리미터는 URL에 다음과 같이 `?`를 사작으로 보낼 수 있다. 다음 파라미터는 `&`로 구분하면 된다.
* http://localhost:8080/request-param?username=hello&age=20

서버에서는 `HttpServletRequest`가 제공하는 다음 메서드를 통해 쿼리 파라미터를 편리하게 조회할 수 있다.

**쿼리 파라미터 조회 메서드**
```java
  String username = request.getParameter("username"); //단일 파라미터 조회
  Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들
  모두 조회
  Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map
  으로 조회
  String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회
```

**복수 파라미터에서 단일 파라미터 조회**

`username=hello&username=kim`과 같이 파라미터 이름은 하나인데, 값이 중복이면 `request.getParameter()`는
하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용해야 한다.
지금처럼 중복일 때는 `request.getParameterValues()`를 사용해야 한다.
참고로 이렇게 중복일 때 `request.getParameter()`를 사용하면 `request.getParameterValues()`의 첫번째 값을 반환한다.


### HTTP 요청 데이터 - POST HTML Form
HTML의 Form 을 사용해서 클라이언트에서 서버로 데이터를 전송
주로 회원가입, 상품 주문 등에서 사용

**특징**
* content-type:`application/x-www-form-urlencoded`
* 메시지 바디에 쿼리 파라미터 형식으로 데이터를 전달한다.`username=hello&age=20`

> **주의**
> 
> 웹 브라우저가 결과를 캐시하고 있어, 과거에 작성했던 html 결과가 보이는 경우가 있음

POST의 HTML Form을 전송하면 웹 브라우저는 다음 형식으로 HTTP 메시지를 만든다.
* 요청 URL: http://localhost:8080/request-param
* **content-type**: `application/x-www-from-urlencoded`
* **message body**: `username=lee&age=23`

`application/x-www-urlencoded`형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다.

따라서 **쿼리 파라미터 조회 메서드를 그대로 사용**하면 된다.

클라이언트 입장에서는 두 방식에 차이가 있지만, 서버 입장에서는 둘의 형식이 동일하므로, `request.getParameter()`로 편리하게 구분없이 조회할 수 있다.

정리하면 `request.getParameter()`는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML FORM 형식도 둘 다 지원한다.


> **참고**
> 
> content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.
> **GET URL 쿼리 파라미터 형식**으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다.
> **POST HTML Form 형식**으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지
> content-type을 꼭 지정해야 한다. 이렇게 폼으로 데이터를 전송하는 형식을 `application/x-www-urlencoded`라 한다.

