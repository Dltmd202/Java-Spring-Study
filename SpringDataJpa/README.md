### 공통 인터페이스 설정

#### JavaConfig 설정 - 스프링 부트 사용시 생략 가능

```java
@Configuration
@EnableJpaRepositories(basePackages = "jpabook.jpashop.repository")
public class AppConfig {}
```

* 스프링부트사용시 `@SpringBootApplication` 위치를지정(해당패키지와하위패키지인식)

* 만약 위치가 달라지면 `@EnableJpaRepositories` 필요

#### 스프링 데이터 JPA가 구현 클래스 대신 생성

![](./res/1.png)

* `org.springframework.data.repository.Repository` 를 구현한 클래스는 스캔 대상 
  * `MemberRepository` 인터페이스가 동작한 이유 
  * `memberRepository.getClass()` -> `class com.sun.proxy.$ProxyXXX`

* `@Repository` 애노테이션 생략 가능 
  * 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리 
  * JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리

### 공통 인터페이스 분석

* JpaRepository 인터페이스: 공통 CRUD 제공
* 제네릭은 <엔티티 타입, 식별자 타입> 설정

* `JpaRepository` 공통 기능 인터페이스

```java
public interface JpaRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>{
    
}
```

* `JpaRepository` 를 사용하는 인터페이스

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

#### 공통 인터페이스 구성

![](./res/2.png)

#### 제네릭 타입
  * `T` : 엔티티 
  * `ID` : 엔티티와 식별자 타입
  * `S` : 엔티티와 그 자식 타입

#### 주요 메서드

* `save(S)` : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다. 
* `delete(T)` : 엔티티 하나를 삭제한다. 내부에서 `EntityManager.remove()` 호출 
* `findById(ID)` : 엔티티 하나를 조회한다. 내부에서 `EntityManager.find()` 호출 
* `getOne(ID)` : 엔티티를 프록시로 조회한다. 내부에서 `EntityManager.getReference()` 호출 findAll(...) : 모든 엔티티를 조회한다. 
  정렬( `Sort` )이나 페이징( `Pageable` ) 조건을 파라미터로 제공할 수 있다.

## 쿼리 메서드 기능

* 메서드 이름으로 쿼리 생성
* 메서드 이름으로 JPA NamedQuery 호출
* `@Query` 어노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의

### 메소드 이름으로 쿼리 생성

메소드 이름을 분석해서 JPQL 쿼리 실행

#### 순수 JPA 레포지토리

```java
public List<Member> findByUsernameAndAgeGreaterThen(String username, int age){
    return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
            .setParameter("username", username)
            .setParameter("age", age)
            .getResultList();
}
```

#### 스프링 데이터 JPA

```java
List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
```

### 쿼리 메소드 필터 조건

스프링 데이터 JPA 공식 문서 참고: 
(https://docs.spring.io/spring-data/jpa/docs/current/ reference/html/#jpa.query-methods.query-creation)

### 스프링 데이터 JPA가 제공하는 쿼리 메소드 기능

* 조회: find...By ,read...By ,query...By get...By,
  * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/ #repositories.query-methods.query-creation 
  * 예:) findHelloBy 처럼 ...에 식별하기 위한 내용(설명)이 들어가도 된다.
  
* COUNT: count...By 반환타입 `long` 
* EXISTS: exists...By 반환타입 `boolean` 
* 삭제: delete...By, remove...By 반환타입 `long` 
* DISTINCT: findDistinct, findMemberDistinctBy 
* LIMIT: findFirst3, findFirst, findTop, findTop3
  * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/ #repositories.limit-query-result

  
> 참고
> 
> 이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다. 
> 그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.
> 이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.

## @Query, 값, DTO 조회하기

#### 단순히 값 하나를 조회

```java
@Query("select m.username from Member m")
List<String> findUsernameList();
```

#### 파라미터 바인딩

```java
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);
}
```

#### 컬렉션 파라미터 바인딩

`Collection` 타입으로 in절 지원

```java
@Query("select m from Member m where m.username in :names")
List<Member> findByNames(@Param("names") List<String> names);
```

#### 반환 타입

스프링 데이터 JPA는 유연한 반환 타입 지원

```java
List<Member> findByUsername(String name); //컬렉션 
Member findByUsername(String name); //단건
Optional<Member> findByUsername(String name); //단건 Optional
```