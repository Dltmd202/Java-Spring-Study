### 프로젝트 생성


#### 스프링 부트 라이브러리

* spring-boot-starter-web 
  * spring-boot-starter-tomcat: 톰캣 (웹서버) 
  * spring-webmvc: 스프링 웹 MVC
* spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View) 
* spring-boot-starter-data-jpa
  * spring-boot-starter-aop
  * spring-boot-starter-jdbc
    * HikariCP 커넥션 풀 (부트 2.0 기본)
  * hibernate + JPA: 하이버네이트 + JPA
  * spring-data-jpa: 스프링 데이터 JPA 
* spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
  * spring-boot 
    * spring-core
  * spring-boot-starter-logging 
    * logback, slf4j

#### 테스트 라이브러리
* spring-boot-starter-test
  * junit: 테스트 프레임워크
  * mockito: 목 라이브러리
  * assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리 
  * spring-test: 스프링 통합 테스트 지원

* 핵심 라이브러리

  * 스프링 MVC 
  * 스프링 ORM
  * JPA, 하이버네이트
  * 스프링 데이터 

* JPA 기타 라이브러리

  * H2 데이터베이스 클라이언트 * 커넥션 풀: 부트 기본은 HikariCP 
  * WEB(thymeleaf)
  * 로깅 SLF4J & LogBack
  * 테스트


## View 환경 설정

#### thymeleaf 템플릿 엔진
* thymeleaf 공식 사이트: https://www.thymeleaf.org/
* 스프링 공식 튜토리얼: https://spring.io/guides/gs/serving-web-content/
* 스프링부트 메뉴얼: https://docs.spring.io/spring-boot/docs/2.1.6.RELEASE/reference/html/ boot-features-developing-web-applications.html#boot-features-spring-mvc-template-

* 스프링 부트 thymeleaf viewName 매핑 
  * `resources:templates/` +{ViewName}+ `.html`

#### JPA와 DB 설정, 동작확인

`main/resources/application.yml`

```yaml
 spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/jpashop
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
         #show_sql: true
        format_sql: true
        
logging.level:
  org.hibernate.SQL: debug
```

##### 회원 엔티티
```java
@Entity
  @Getter @Setter
  public class Member {
      @Id @GeneratedValue
      private Long id;
      private String username;
}
```

##### 회원 레포지토리
```java
package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}

```

##### 테스트
```java
package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
```

> 참고: 스프링부트를 통해 복잡한 설정이 다 자동화 되었다.`persistence.xml`도 없고,
> `LocalContainerEntityManagerFactoryBean`도 없다. 
> 스프링 부트를 통한 추가 설정은 스프링 부트 메뉴얼을 참고

#### 쿼리 파라미터 로그 남기기

* 로그에 다음을 추가하기 `org.hibernate.type` : SQL 실행 파라미터를 로그로 남긴다.
* 외부 라이브러리 사용
  * https://github.com/gavlyukovskiy/spring-boot-data-source-decorator

```
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6'
```

> 참고: 쿼리 파라미터를 로그로 남기는 외부 라이브러리는 시스템 자원을 사용하므로, 개발 단계에서는 편하 게 사용해도 된다. 
> 하지만 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다.
