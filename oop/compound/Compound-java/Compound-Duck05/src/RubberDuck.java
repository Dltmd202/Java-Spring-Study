/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * RubberDuck.java
 * 복합 패턴
 * 장난감 고무오리
 */
public class RubberDuck implements Quackable {
	@Override
	public void quack() {
		System.out.println("고무오리 >> 삑삑");
	}
}
