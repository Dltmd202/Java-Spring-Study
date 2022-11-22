/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * SoldOutState.java
 * 상태 객체
 * @author 김상진
 */
public class SoldOutState implements GumballState {
	
	@Override
	public void insertCoin(GumballMachine gumballMachine) {
		System.out.println("껌볼이 없어 판매가 중단됨");
	}
	
	@Override
	public void ejectCoin(GumballMachine gumballMachine) {
		System.out.println("껌볼이 없어 판매가 중단됨");
	}
	
	@Override
	public void turnCrank(GumballMachine gumballMachine) {
		System.out.println("껌볼이 없어 판매가 중단됨");
	}
	
	@Override
	public void dispense(GumballMachine gumballMachine) {
		System.out.println("껌볼이 없어 판매가 중단됨");
	}
	
	@Override
	public void refill(GumballMachine gumballMachine) {
		System.out.println("껌볼을 채움");
		gumballMachine.refill(20);
		gumballMachine.changeToNoCoinState();
	}
}
