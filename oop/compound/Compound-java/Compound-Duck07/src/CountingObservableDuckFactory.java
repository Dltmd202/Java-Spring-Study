/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 복합 패턴: 팩토리 패턴???
 * 오리를 생성할 때 QuackCounter 장식자를 사용하도록 함
 */
public class CountingObservableDuckFactory extends CountingDuckFactory {
	
	@Override
	public Quackable createMallardDuck() {
		return new DuckObservable(super.createMallardDuck());
	}

	@Override
	public Quackable createRedheadDuck() {
		return new DuckObservable(super.createRedheadDuck());
	}

	@Override
	public Quackable createDuckCall() {
		return new DuckObservable(super.createDuckCall());
	}

	@Override
	public Quackable createRubberDuck() {
		return new DuckObservable(super.createRubberDuck());
	}
}
