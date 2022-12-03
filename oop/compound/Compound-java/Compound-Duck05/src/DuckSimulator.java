/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * DuckSimulator.java
 * 복합 패턴
 * 버전 5.	
 * 	오리를 개별적으로 처리하지 않고 
 * 	오리떼를 하나의 오리와 동일하게 처리하도록 Composite 패턴 활용
 */
public class DuckSimulator {
	public static void main(String[] args){
		DuckSimulator simulator = new DuckSimulator();
		AbstractDuckFactory duckFactory = new CountingDuckFactory();
		simulator.simulate(duckFactory);
	}
	public void simulate(AbstractDuckFactory duckFactory){
		Quackable mallardDuck = duckFactory.createMallardDuck();
		// 오리떼 생성
		// Flock은 개별 오리와 오리떼 자체를 요소로 유지할 수 있음
		Flock flockOfDucks = new Flock();
		flockOfDucks.add(mallardDuck);
		Flock flockOfRedheads = new Flock();
		flockOfRedheads.add(duckFactory.createRedheadDuck());
		flockOfRedheads.add(duckFactory.createRedheadDuck());
		flockOfDucks.add(flockOfRedheads);
		simulate(flockOfDucks);
		// 기존 시뮬레이션
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
