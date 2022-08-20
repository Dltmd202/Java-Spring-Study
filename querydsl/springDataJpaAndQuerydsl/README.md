# 스프링 데이터 JPA가 제공하는 Querydsl 기능

## 인터페이스 지원 - QuerydslPredicateExecutor

#### QuerydslPredicateExecutor 인터페이스

```java
/*
 * Copyright 2011-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.querydsl;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/**
 * Interface to allow execution of QueryDsl {@link Predicate} instances.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public interface QuerydslPredicateExecutor<T> {

	/**
	 * Returns a single entity matching the given {@link Predicate} or {@link Optional#empty()} if none was found.
	 *
	 * @param predicate must not be {@literal null}.
	 * @return a single entity matching the given {@link Predicate} or {@link Optional#empty()} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the predicate yields more than one
	 *           result.
	 */
	Optional<T> findOne(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate}. In case no match could be found an empty
	 * {@link Iterable} is returned.
	 *
	 * @param predicate must not be {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 */
	Iterable<T> findAll(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link Sort}. In case no match could
	 * be found an empty {@link Iterable} is returned.
	 *
	 * @param predicate must not be {@literal null}.
	 * @param sort the {@link Sort} specification to sort the results by, may be {@link Sort#unsorted()}, must not be
	 *          {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 * @since 1.10
	 */
	Iterable<T> findAll(Predicate predicate, Sort sort);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s. In case no
	 * match could be found an empty {@link Iterable} is returned.
	 *
	 * @param predicate must not be {@literal null}.
	 * @param orders the {@link OrderSpecifier}s to sort the results by, must not be {@literal null}.
	 * @return all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s.
	 */
	Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	/**
	 * Returns all entities ordered by the given {@link OrderSpecifier}s.
	 *
	 * @param orders the {@link OrderSpecifier}s to sort the results by, must not be {@literal null}.
	 * @return all entities ordered by the given {@link OrderSpecifier}s.
	 */
	Iterable<T> findAll(OrderSpecifier<?>... orders);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Predicate}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 *
	 * @param predicate must not be {@literal null}.
	 * @param pageable may be {@link Pageable#unpaged()}, must not be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Predicate}.
	 */
	Page<T> findAll(Predicate predicate, Pageable pageable);

	/**
	 * Returns the number of instances matching the given {@link Predicate}.
	 *
	 * @param predicate the {@link Predicate} to count instances for, must not be {@literal null}.
	 * @return the number of instances matching the {@link Predicate}.
	 */
	long count(Predicate predicate);

	/**
	 * Checks whether the data store contains elements that match the given {@link Predicate}.
	 *
	 * @param predicate the {@link Predicate} to use for the existence check, must not be {@literal null}.
	 * @return {@literal true} if the data store contains elements that match the given {@link Predicate}.
	 */
	boolean exists(Predicate predicate);

	/**
	 * Returns entities matching the given {@link Predicate} applying the {@link Function queryFunction} that defines the
	 * query and its result type.
	 *
	 * @param predicate must not be {@literal null}.
	 * @param queryFunction the query function defining projection, sorting, and the result type
	 * @return all entities matching the given {@link Predicate}.
	 * @since 2.6
	 */
	<S extends T, R> R findBy(Predicate predicate, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}
```

#### 리포지토리에 적용

```java
package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> ,
        MemberRepositoryCustom,
        QuerydslPredicateExecutor<Member> {

    List<Member> findByUsername(String username);
}
```

```java
Iterable<Member> result = memberRepository.findAll(
        member.age.between(20, 40)
        .and(member.username.eq("member1"))
        );
```

### 한계점
* 조인X (묵시적 조인은 가능하지만 left join이 불가능하다.)
* 클라이언트가 Querydsl에 의존해야 한다. 서비스 클래스가 Querydsl이라는 구현 기술에 의존해야 한다. 
* 복잡한 실무환경에서 사용하기에는 한계가 명확하다.


> 참고
> 
> `QuerydslPredicateExecutor` 는 Pagable, Sort를 모두 지원하고 정상 동작한다.


## Querydsl Web 지원

* 공식 URL: `https://docs.spring.io/spring-data/jpa/docs/2.2.3.RELEASE/reference/html/#core.web.type-safe`


#### 한계점
* 단순한 조건만 가능 
* 조건을 커스텀하는 기능이 복잡하고 명시적이지 않음 
* 컨트롤러가 Querydsl에 의존 
* 복잡한 실무환경에서 사용하기에는 한계가 명확

## 리포지토리 지원 - QuerydslRepositorySupport


#### 장점
* `getQuerydsl().applyPagination()` 스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환 가능(단! Sort는 오류발생)
* `from()` 으로 시작 가능(최근에는 QueryFactory를 사용해서 select() 로 시작하는 것이 더 명시적) 
* EntityManager 제공

#### 한계
* Querydsl 3.x 버전을 대상으로 만듬 
* Querydsl 4.x에 나온 JPAQueryFactory로 시작할 수 없음 
  * select로 시작할 수 없음 (from으로 시작해야함) 
* `QueryFactory` 를 제공하지 않음 
* 스프링 데이터 Sort 기능이 정상 동작하지 않음


## Querydsl 지원 클래스 직접 만들기

스프링 데이터가 제공하는 QuerydslRepositorySupport 가 지닌 한계를 극복하기 위해 직접 Querydsl
지원 클래스를 만든다.

#### 장점

* 스프링 데이터가 제공하는 페이징을 편리하게 변환 
* 페이징과 카운트 쿼리 분리 가능 
* 스프링 데이터 Sort 지원 
* `select()` , `selectFrom()` 으로 시작 가능 
* `EntityManager` , `QueryFactory` 제공


#### Querydsl4RepositorySupport

```java

```