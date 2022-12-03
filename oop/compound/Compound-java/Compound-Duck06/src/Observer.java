/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * Observer.java
 * 복합 패턴: 관찰자 패턴 
 * 개별 오리가 꽥꽥했을 때 그것을 자동으로 알고 싶은 객체들이 구현해야 하는 메소드 
 * pull 방법: 꽥꽥한 오리자체를 전달함
 */
public interface Observer {
	void update(QuackObservable duck);
}
