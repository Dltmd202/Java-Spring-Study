/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * Observer.java
 * 복합 패턴: 관찰자 패턴 
 * 개별 오리의 꽥꽥을 관찰하고자 하는 관찰자: pull 방법
 * @author 김상진
 */
public class Quackologist implements Observer {
	@Override
	public void update(QuackObservable duck) {
		System.out.println("오리학자: " + duck + " 방금 꽥꽥함");
	}
}
