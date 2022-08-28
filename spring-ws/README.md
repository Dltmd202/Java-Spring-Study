# WebSocket을 사용하여 대화형 웹 애플리케이션 구축


이 가이드는 브라우저와 서버 간에 메시지를 주고받는 "Hello, World" 애플리케이션을 만드는 과정을 안내한다.
WebSocket은 TCP 위의 얇고 가벼운 계층이다. 이는 메시지를 포함하기 위해 "서브 프로토콜"을 사용하는 데 적합하다.
이 가이드는 Spring과 `STOMP` 메시징을 상용하여 대화형 웹 애플리케이션을 만든다. STOMP는 하위 수준 WebSocket 위에서
작동하는 하위 프로토콜이다.

## 무엇을 만드는가


사용자 이름이 포함된 메시지를 수락하는 서버를 구축한다. 이에 대한 응답으로는 서버는 클라이언트가 구독 중인 대기열에 인사말을 푸시한다.

## 종속성 추가

```groovey
plugins {
	id 'org.springframework.boot' version '2.7.3'
	id 'io.spring.dependency-management' version '1.0.13.RELEASE'
	id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-websocket'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'

	implementation 'org.webjars:webjars-locator-core'
	implementation 'org.webjars:sockjs-client:1.0.2'
	implementation 'org.webjars:stomp-websocket:2.3.3'
	implementation 'org.webjars:bootstrap:3.3.7'
	implementation 'org.webjars:jquery:3.1.1-1'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

### 리소스 표현 클래스 생성

이제 프로젝트와 빌드 시스템을 설정했으므로 STOMP 메시지 서비스를 만들 수 있다.

서비스 상호 작용에 대해 생각하여 프로세스를 시작한다.

서비스는 본문이 JSON 개체인 STOMP 메시지에 `name`이 포함된 메시지를 수락한다.
`name`이 `Fred`인 경우 메시지는 다음과 유사할 수 있습니다.

```json
{
    "name": "Fred"
}
```

이름이 포함된 메시지를 모델링하려면 다음 목록(from) 에서 볼 수 있듯이 `name`속성 및 해당 메서드가 있는 일반 이전 
Java 객체를 만들 수 있다.

```java
package com.example.initial;

public class HelloMessage {
    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

메시지를 수신하고 이름을 추출하면 서비스는 인사말을 만들고 클라이언트가 구독하는 별도의 대기열에 해당 인사말을 게시하여 메시지를 처리한다. 
인사말은 다음 목록과 같이 JSON 객체일 수 있다.

```json
{
    "content": "Hello, Fred!"
}
```


인사말 표현을 모델링하려면 다음 목록(from) 에서 볼 수 있듯이 `content`속성 및 해당 메서드 가 있는 다른 일반 이전 Java 객체를 추가한다.


```java
package com.example.initial;

public class Greeting {
    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

Spring은 Jackson JSON 라이브러리를 사용하여 유형의 인스턴스를 `Greeting` JSON 으로 자동 직렬화합니다.


다음으로 Hello 메시지를 수신하고 인사말 메시지를 보내는 컨트롤러를 생성합니다.


### 메시지 처리 컨트롤러 만들기

STOMP 메시징 작업에 대한 Spring의 접근 방식에서 STOMP 메시지는 `@Controller`클래스로 라우팅될 수 있다. 

```java
package com.example.initial;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws InterruptedException {
        Thread.sleep(1000);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
```

이 컨트롤러는 간결하고 간단하지만 많은 일이 진행 중이다. 단계적으로 보면


1. `@MessageMapping` 어노테이션은 메시지가 `/hello`를 대상으로 전송되면 `greeting()`메서드가 호출 되도록 한다.
2. 메시지의 페이로드 `HelloMessage`는 `greeting()`에 전달되는 개체에 바인딩됩니다.
3. 내부적으로 메서드 구현은 스레드가 1초 동안 `sleep` 상태로 전환되도록 하여 처리 지연을 시뮬레이션합니다. 
   이는 클라이언트가 메시지를 보낸 후 서버가 메시지를 비동기식으로 처리해야 하는 만큼 시간이 걸릴 수 
   있음을 보여주기 위한 것이다. 클라이언트는 응답을 기다리지 않고 필요한 작업을 계속할 수 있습니다.
4. 1초 지연 후 `greeting()`메서드는 `Greeting` 객체를 만들고 반환합니다. 
   반환 값은 주석 `/topic/greetings`에 지정된 대로 의 모든 구독자에게 브로드캐스트된다. 
   `@SendTo`입력 메시지의 이름은 삭제됩니다. 이 경우 클라이언트 측의 브라우저 DOM에서 다시 에코되고 다시 렌더링되기 때문입니다. 

### STOMP 메시징을 위한 Spring 구성

```java
package com.example.initial;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
}
```


`@Configuration` 애노테이션이 붙은 `WebSocketConfig`은 Spring Configuration class이다. 
`@EnableWebSocketMessageBroker`로 주석을 달기도 한다.
이름에서 알 수 있듯이 `@EnableWebSocketMessageBroker`는 메시지 브로커가 지원하는 WebSocket 메시지 처리를 활성화합니다.


`configureMessageBroker()` 메서드를 구현함을 통해 `WebSocketMessageBrokerConfigurer`를 메시지 브로커로서 구성한다.