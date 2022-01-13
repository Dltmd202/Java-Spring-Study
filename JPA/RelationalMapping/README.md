## 연관관계 매핑 기초

### 단방향 연관관계

![](./RelationalMapping/1.png)

* 객체 연관관계
  * 회원 객체는 `Member.team` 필드로 팀 객체와 연관관계를 맺는다.
  * 회원 객체와 팀 객체는 단방향 관계다. 회원은 `Member.team` 필드를 통해서 팀을 알 수 있지만 반대로 팀은 회원을 알 수 없다.

* 테이블 연관관계
  * 회원 테이블은 `TEAM_ID` 외래 키로 팀 테이블과 연관관계를 맺는다.
  * 회원 테이블과 팀 테이블은 양방향 관계다. 회원 테이블 `TEAM_ID` 외래 키를 통해서 회원과 팀을 조인할 수 있고 
  반대로 팀과 회원을 조인할 수 있다.

#### 객체 관계 매핑

![](./RelationalMapping/2.png)

```java

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NAME")
    @JoinColumn(name = "TEAM_ID")
    private String name;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
```

* `@ManyToOne`: 이름 그대로 다대일(1:N) 관계라는 매핑 정보다. 회원과 팀은 다대일 관계다. 연관관계를 매핑할 때 이렇게
  다중성을 나타내는 어노테이션을 필수로 사용해야 한다.


* `@JoinColumn(name = "TEAM_ID")`: 조인 칼럼은 외래 키를 매핑할 때 사용한다. `name` 속성에는 매핑할 외래 키
  이름을 지정한다. 회원과 팀 테이블은 `TEAM_ID` 외래키로 연관관계를 맺으므로 이 값을 지정하면 된다. 이 어노테이션은 생략할
  수 있다.




```java
Team team = new Team();
team.setName("TeamA");
em.persist(team);

Member member = new Member();
member.setName("member1");
member.setTeam(team );
em.persist(member);

Member findMember = em.find(Member.class, member.getId());

Team findTeam = findMember.getTeam();
System.out.println("findTeam = " + findTeam);

tx.commit();
```