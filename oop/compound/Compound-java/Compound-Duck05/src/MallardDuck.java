/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * MallardDuck.java
 * 복합 패턴
 * 청둥오리
 */
public class MallardDuck implements Quackable {
	@Override
	public void quack() {
		System.out.println("청둥오리 >> 꽥꽥");
	}
}
