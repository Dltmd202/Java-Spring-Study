/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * AbstractDuckFactory.java
 * 복합 패턴: 
 * 오리를 생성할 때 특정한 방법으로 생성하도록 하기 위한 interface
 */
public interface AbstractDuckFactory {
	Quackable createMallardDuck();
	Quackable createRedheadDuck();
	Quackable createDuckCall();
	Quackable createRubberDuck();
}
