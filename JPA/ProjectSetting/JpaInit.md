## Hello JPA - 프로젝트 생성

### H2 데이터베이스 설치와 실행

* http://www.h2database.com/
* 실습용 DB
* 가벼움 (1.5MB)
* 웹용 쿼리툴 제공
* MySQL, Oracle 데이터베이스 시뮬레이션 기능
* 시퀸스, AUTO INCREMENT 기능 지원

### 프로젝트 생성
* **자바 8 이상**
* 메이븐 설정
  * **groupdId**: jpa-basic
  * **artifactOd**: ex1-hello-jpa
  * **version**: 1.0.0

* 라이브러리 추가 - `pom.xml`
    ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>jpa-basic</groupId>
    <artifactId>ex1-hello-jpa</artifactId>
    <version>1.0.0</version>
  
    <dependencies>
      <!-- JPA 하이버네이트 -->
      <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-entitymanager</artifactId>
        <version>5.3.10.Final</version>
      </dependency>
  
      <!-- H2 데이터베이스 -->
      <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.0.204</version>
      </dependency>
    </dependencies>
  </project>
  ```

* JPA 설정하기 - `persistence.xml`
  * JPA 설정 파일
  * `/META-INF/persistence.xml` 위치
  * `persistence-unit` `name`으로 이름 지정
  * `javax.persistence`로 시작: JPA 표준 속성
  * hibernate로 시작: 하이버네이트 전용 속성

* `persistence.xml`
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <persistence version="2.2"
  xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
      <properties>
          <!-- 필수 속성 -->
          <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
          <property name="javax.persistence.jdbc.user" value="sa"/>
          <property name="javax.persistence.jdbc.password" value=""/>
          <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
          <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
          <!-- 옵션 -->
          <property name="hibernate.show_sql" value="true"/>
          <property name="hibernate.format_sql" value="true"/>
          <property name="hibernate.use_sql_comments" value="true"/>
          <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
      </properties>
    </persistence-unit>
  </persistence>
  ```
  
* 데이터베이스 방언
  * JPA는 특정 데이터베이스에 종속 X
  * 각각의 데이터테이스가 제공하는 SQL 문법과 함수는 조금씩 다름
  * 가변 문자: `MySQL`은 `VARCHAR`, `Oracle`은 `VARCHAR2`
  * 문자열을 자르는 함: `SQL` 표준은 `SUBSTRING()`, `Oracle`은 `SUBSTR()`
  * 페이징: `MySQL`은 `LIMIT`, `Oracle`은 `ROWNUM`
  * 방언: `SQL` 표준을 지키지 않는 특정 데이터베이스만의 교유한 기능
  * `hibernate.dialect` 속성에 지정
    * `H2`: `org.hibernate.dialect.H2Dialect`
    * `Oracle`: `org.hibernate.dialect.Oracle10gDialect`
    * `MySQL`: `org.hibernate.dialect.MySQL5InnoDBDialect`
  * 하이버네이트는 40가지 이상의 데이터베이스 방언 지원
  