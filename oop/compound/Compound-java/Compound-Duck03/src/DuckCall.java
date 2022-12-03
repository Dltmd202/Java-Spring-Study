/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * DuckCall.java
 * 복합 패턴
 * 오리 소리 흉내 클래스 
 */
public class DuckCall implements Quackable {
	@Override
	public void quack() {
		System.out.println("꽥액액~~");
	}
}
