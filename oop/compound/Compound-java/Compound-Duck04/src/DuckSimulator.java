/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * DuckSimulator.java
 * 복합 패턴
 * 오리를 생성할 때 주어진 팩토리를 이용하도록 함 (강제화는 어떻게?)
 */
public class DuckSimulator {
	public static void main(String[] args){
		DuckSimulator simulator = new DuckSimulator();
		AbstractDuckFactory duckFactory = new CountingDuckFactory();
		simulator.simulate(duckFactory);
	}
	public void simulate(AbstractDuckFactory duckFactory){
		Quackable mallardDuck = duckFactory.createMallardDuck();
		simulate(mallardDuck);
		Quackable redheadDuck = duckFactory.createRedheadDuck();
		simulate(redheadDuck);
		Quackable duckCall = duckFactory.createDuckCall();
		simulate(duckCall);
		Quackable rubberDuck = duckFactory.createRubberDuck();
		simulate(rubberDuck);
		Quackable goose = new GooseAdapter(new Goose());
		simulate(goose);
		System.out.printf("꽥꽥 수: %d%n", QuackCounter.getQuacks());	
	}
	private void simulate(Quackable duck){
		duck.quack();
	}
}
