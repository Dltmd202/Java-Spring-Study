## 프록시와 연관관계 매핑

### 프록시

#### Member를 조회할 때 Team도 함께 조회를 해야할까??

```java
public void printUserAndTeam(String memberId) {
    Member member = em.find(Member.class, memberId);
    Team team = member.getTeam();
    System.out.println("회원 이름: " + member.getUsername());
    System.out.println("소속팀: " + team.getName()); 
}
```

```java
public void printUser(String memberId) {
    Member member = em.find(Member.class, memberId);
    Team team = member.getTeam();
    System.out.println("회원 이름: " + member.getUsername());
}
```

### 프록시 기초

* `em.find()` vs `em.getReference()`
* `em.find()` : 데이터베이스를 통해서 실제 인티티 객체 조회
* `em.getReference()` : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회

### 프록시 특징

* 실제 클래스를 상속 받아서 만들어짐
* 실제 클래스와 겉 모양이 같다.
* 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨

### 프록시 객체의 초기화

```java
Member member = em.getReference(Member.class, “id1”); 
member.getName();
```

![](./proxy/1.png)

### 프록시의 특징

* 프록시 객체는 처음 사용할 때 한 번만 초기화

* 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초 기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능

* #### 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (== 비 교 실패, 대신 `instance of` 사용)
* 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 `em.getReference()`를 호출해 도 실제 엔티티 반환
* 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
(하이버네이트는 `org.hibernate.LazyInitializationException` 예외를 터트림)

### 프록시 확인

* 프록시 인스턴스의 초기화 여부 확인 
  PersistenceUnitUtil.isLoaded(Object entity)

* 프록시 클래스 확인 방법
  `entity.getClass().getName()`
* 프록시 강제 초기화
 `org.hibernate.Hibernate.initialize(entity);`
> 참고: JPA 표준은 강제 초기화 없음
> 
>   강제 호출: `member.getName()`
