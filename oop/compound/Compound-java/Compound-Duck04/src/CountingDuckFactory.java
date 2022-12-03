/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * CountingDuckFactory.java
 * 복합 패턴
 * 오리를 생성할 때 QuackCounter 장식자를 사용하도록 함
 */
public class CountingDuckFactory implements AbstractDuckFactory {
	@Override
	public Quackable createMallardDuck() {
		return new QuackCounter(new MallardDuck());
	}
	@Override
	public Quackable createRedheadDuck() {
		return new QuackCounter(new RedheadDuck());
	}
	@Override
	public Quackable createDuckCall() {
		return new QuackCounter(new DuckCall());
	}
	@Override
	public Quackable createRubberDuck() {
		return new QuackCounter(new RubberDuck());
	}
}
