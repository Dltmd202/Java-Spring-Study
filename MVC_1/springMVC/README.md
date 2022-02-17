
# MVC 프레임워크 만들기

## 프론트 컨트롤러 패턴 소개

### 프론트 컨트롤러 도입 전
![](./res/1.png)

### 프론트 컨트롤러 도입 후
![](./res/2.png)

### FrontController 패턴 특징

* 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음 
* 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
* 입구를 하나로!
* 공통 처리 가능
* 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

### 스프링 웹 MVC와 프론트 컨트롤러
* 스프링 웹 MVC의 핵심도 바로 `FrontController`
* 스프링 웹 MVC의 `DispatcherServlet`이 `FrontController` 패턴으로 구현되어 있음


## 프론트 컨트롤러 도입 - v1

![](./res/3.png)

서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다. 각 컨트롤러들은 이 인터페이스를 구현하면 된다. 
프론트 컨트롤러는 이 인테페이스를 호출해서 구현과 관계업이 로직의 일관성을 가져갈 수 있다.


### urlpatterns
* `urlPatterns = "/front-controller/v1/*"` : `/front-controller/v1` 를 포함한 하위 모든 요청은 이 서블릿에서 받아들인다.
* 예) `/front-controller/v1` , `/front-controller/v1/a` ,` /front-controller/v1/a/b`


### controllerMap

* key: 매핑 URL
* value: 호출될 컨트롤러

### service()

먼저 requestURI 를 조회해서 실제 호출할 컨트롤러를 `controllerMap` 에서 찾는다. 
만약 없다면 `404(SC_NOT_FOUND)` 상태 코드를 반환한다.
컨트롤러를 찾고` controller.process(request, response);` 을 호출해서 해당 컨트롤러를 실행한다.

### View 분리 - V2

![](./res/4.png)

## Model 추가

### 서블릿 종속성 제거

> 컨트롤러 입장에서 `HttpServletRequest`, `HttpServletResponse이` 꼭 필요할까?
> 
> 요청 파라미터 정보는 자바의 `Map`으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
> 그리고 request 객체를 `Model`로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다. 우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해보자.
> 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.

### 뷰 이름 중복 제거

컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 하자.
이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.

* `/WEB-INF/views/new-form.jsp` -> `new-form`
* `/WEB-INF/views/save-result.jsp` -> `save-result `
* `/WEB-INF/views/members.jsp` -> `members`
      
![](./res/5.png)

### ModelView

지금까지 컨트롤러에서 서블릿에 종속적인 HttpServletRequest를 사용했다. 그리고 Model도 request.setAttribute() 를 통해
데이터를 저장하고 뷰에 전달했다.

서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들어보자.
