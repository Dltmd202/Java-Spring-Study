# JPQL

### 문법

```sql
select_문 :: = select_절
from_절 [where_절] [groupby_절] [having_절] [orderby_절]
update_문 :: = update_절 [where_절] delete_문 :: = delete_절 [where_절]
```

* `select m from Member as m where m.age > 18`
* 엔티티와 속성은 대소문자 구분 O (Member, age)
* JPQL 키워드는 대소문자 구분 X (SELECT, FROM)
* 엔티티 이름 사용, 테이블 이름이 아님(Member)
* 별칭이 필수(m) (as는 생략가능)

### 집합과 정렬

```sql
SELECT
    Count(m),
    SUM(m.age),
    AVG(m.age),
    MAX(m.age),
    MIN(m.age)
FROM Member m
```

* `Group By`, `Having`
* `Order By`

### TypeQuery, Query

* TypeQuery: 반환 타입이 명확할 때 사용

```java
TypedQuery<Member> query = 
        em.createQuery("SELECT m FROM Member m", Member.class);
```

* Query: 반환 타입이 명확하지 않을 때 사용

```java
Query query =
        em.createQuery("SELECT m.username, m.age from Member m");
```

### 결과 조회 API

* `query.getResultList()`: 결과가 하나 이상일 때, 리스트 반환
  * 결과가 없으면 빈 리스트 반환
* `query.getSingleResult()`: 결과가 정확히 하나, 단일 객체 반환
  * 결과가 없으면: `javax.persistence.NoResultException`
  * 둘 이상이면: `javax.persistence.NonUniqueResultException`

### 파라미터 바인딩 - 이름 기준, 위치 기준

```java
query.setParameter("username", usernameParam);
```

## 프로젝션

* `SELECT` 절에 조회할 대상을 지정하는 것
* 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)
* `SELECT m FROM Member m` -> 엔티티 프로젝션
* `SELECT m.team FROM Member m` -> 엔티티 프로젝션
* `SELECT m.address FROM Member m` -> 임베디드 타입 프로젝션
* `SELECT m.username, m.age FROM Member m` -> 스칼라 타입 프로젝션
* `DISTINCT`로 중복 제거

### 프로젝션 - 여러 값 조회

* `SELECT m.username, m.age FROM Member m`
  1. Query 타입으로 조회
  2. Object[] 타입으로 조회

  ```java
  List<Object[]> resultList = em.createQuery(
                        "select m.username, m.age from Member m")
              .getResultList();
  Object[] result = resultList.get(0);
  System.out.println("username = " + result[0]);
  System.out.println("age = " + result[1]);
  ```

  3. new 명령어로 조회
     * 단순히 DTO로 바로 조회
     * SELECT
  
  ```java
    class MemberDto{
    ...
    }
  
    List<Object[]> resultList = em.createQuery(
                        "select new jpql.MemberDto(m.username, m.age)" +
                         " from Member m", MemberDto.class)
              .getResultList();
  ```

## 페이징 API
* JPA는 페이징을 다음 두 API로 추상화
* `setFirstResult(int startPosition)`: 조회 시작 위치(0부터 시작)
* `setMaxResults(int maxResult)`: 조회할 데이터 수

## 조인

* 내부 조인: `SELECT m FROM Member m [INNER] JOIN m.team t`
* 외부 조인: `SELECT m FROM Member m LEFT [OUTER] JOIN m.team t`
* 세타 조인: `SELECT count(m) FROM Member m, Team t where m.username = t.name`

## 조인 - ON 절

* ON 절을 활용한 조인(JPA 2.1부터 지원)
  1. 조인 대상 필터링
  2. 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)

### 조인 대상 필터링

* 예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인

* JPQL: `SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'`
* SQL: `SELECT m.*, t.* FROM Member m LEFT JOIN t ON m.TEAM_ID = t.id and t.name = 'A'`


## 연관관계가 없는 엔티티 외부 조인

* 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인


* JPQL: `SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name`
* SQL: `SELECT m.*, t.* FROM Member m LEFT JOIN t ON m.username = t.name`


## 서브 쿼리

* 나이가 평균보다 많은 회원: `select m from Member m where m.age > (select avg(m2.age) from Member m2)`

* 한 건이라도 주문한 고객: `select m from Member m
  where (select count(o) from Order o where m = o.member) > 0`

### 서브 쿼리 지원 함수

* [NOT] EXIST (subquery): 서브 쿼리에 결과가 존재하면 참
  * {ALL | ANY | SOME } (subquery)
  * ALL 모두 만족하면 참
  * ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
* [NOT] IN (subquery): 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참 

### 서브 쿼리 - 예시

* 팀A 소속인 회원 : 
`select m from Member m
where exists (select t from m.team t where t.name = ‘팀A')`

* 전체 상품 각각의 재고보다 주문량이 많은 주문들: 
`select o from Order o 
where o.orderAmount > ALL (select p.stockAmount from Product p)`

* 어떤 팀이든 팀에 소속된 회원: 
`select m from Member m where m.team = ANY (select t from Team t)`

### JPA 서브 쿼리 한계

* JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
* SELECT 절도 가능(하이버네이트에서 지원)
* FROM 절의 서브 쿼리는 현재 JPQL에서 불가능
  * 조인으로 풀 수 있으면 풀어서 해결

### JPQL 타입 표현

* 문자: 'HELLO', 'She''s'
* 숫자: 10L, 10D, 10F
* Boolean: TRUE, FALSE
* ENUM: com.example.hi.MemberType.Admin(패키지명 포함)
* 엔티티 타입: TYPE(m) = Member (상속 관계에서 사용)

### JPQL 기타

* SQL과 문법이 같은 식
* EXISTS, IN
* AND, OR, NOT
* =, >, >=, <=, <>
* BETWEEN, LIKE, IS NULL

### 조건식 - CASE 식

* 기본 CASE 식

```java
em.createQuery(
        "select" +
        " case when m.age <= 10 then '학생요금'" +
        "      when m.age >= 60 then '경로요금'" +
        "      else '일반요금'" + 
        " end" +
        " from Team t"
        )
```

* COALESCE: 하나씩 조회해서 null이 아니면 반환
```java
em.createQuery(
        "select" +
        " coalesce(m.username. '이름 없는 회원')"
        " from Member m"
        )
```

* NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
```java
em.createQuery(
        "select" +
        " nullif(m.username. '관리자')"
        " from Member m"
        )
```

### JPQL 기본 함수

> JPQL이 제공하는 표준 함수 데이터베이스에 관계없이 사용가능 

* CONCAT : 문자열 합치기

```java
String query1 = "select 'a' || 'b' From Member m";

String query2 = "select concat('a', 'b') From Member m";
```

* SUBSTRING : 서브스트링

```java
String query1 = "select substring(m.username, 2, 3) From Member m";

```

* TRIM : 공백 제거

* LOWER, UPPER : 케이스 변환

* LENGTH : 길이

* LOCATE

```java
String query1 = "select locate('de', 'abcde') From Member m";
List<Integer> result = em.createQuery(query, Integer.class)
        .getResultList;

// 4
```

* ABS, SQRT, MOD

* SIZE, INDEX

```java
String query = "select size(t.members) From Team t";
```

### 사용자 정의 함수

* 방언 등록

```java
public class MyH2Dilect extends H2Dialect {
    
    public MyH2Dilect {
        registerFunction("group_concat", 
                new StandardSQLFunction("group_concat", StandardBasicType.STRING));
    }
}
```

* Persistence.xml에 방언 소스 등록

```xml
<properties>
    <!-- 필수 속성 -->
    <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
    <property name="javax.persistence.jdbc.user" value="sa"/>
    <property name="javax.persistence.jdbc.password" value=""/>
    <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
  
    <!-- 방언 등록  -->
    <property name="hibernate.dialect" value="dialect.MyH2Dialect"/>
  
  
    <property name="hibernate.show_sql" value="true"/>
    <property name="hibernate.format_sql" value="true"/>
    <property name="hibernate.use_sql_comments" value="true"/>
    <property name="hibernate.hbm2ddl.auto" value="create" />
</properties>
```

* 쿼리 실행

```java
String querty1 = "select function('group_concat', m.username) From Member m";
        
// 하이버네이트 지원
String querty2 = "select group_concat(m.username) From Member m";
```
