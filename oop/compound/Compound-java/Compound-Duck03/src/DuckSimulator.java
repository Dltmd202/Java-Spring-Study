/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * DuckSimulator.java
 * 복합 패턴
 * 오리 예제 테스트 프로그램
 * 버전 3. 꽥꽥 횟수를 계산할 수 있는 장식 패턴 활용
 */
public class DuckSimulator {
	public static void main(String[] args){
		DuckSimulator simulator = new DuckSimulator();
		simulator.simulate();
	}
	//  모든 객체를 장식자로 포장하여 사용함
	public void simulate(){
		Quackable mallardDuck = new QuackCounter(new MallardDuck());
		simulate(mallardDuck);
		Quackable redheadDuck = new QuackCounter(new RedheadDuck());
		simulate(redheadDuck);
		Quackable duckCall = new QuackCounter(new DuckCall());
		simulate(duckCall);
		Quackable rubberDuck = new QuackCounter(new RubberDuck());
		simulate(rubberDuck);
		Quackable goose = new GooseAdapter(new Goose());
		simulate(goose);
		System.out.printf("꽥꽥 수: %d%n", QuackCounter.getQuacks());	
	}
	private void simulate(Quackable duck){
		duck.quack();
	}
}
