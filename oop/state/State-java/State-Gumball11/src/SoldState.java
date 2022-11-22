/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * SoldState.java
 * 상태 객체
 * @author 김상진
 */
public class SoldState implements GumballState {
	@Override
	public boolean insertCoin(GumballMachine gumballMachine) {
		System.out.println("동전을 투입할 수 있는 단계가 아님");
		return false;
	}

	@Override
	public boolean ejectCoin(GumballMachine gumballMachine) {
		System.out.println("반환할 동전이 없음");
		return false;
	}

	@Override
	public boolean turnCrank(GumballMachine gumballMachine) {
		System.out.println("이미 손잡이를 돌렸음");
		return false;
	}

	@Override
	public boolean dispense(GumballMachine gumballMachine) {
		System.out.println("껌볼이 나옴");
		gumballMachine.dispense();
		return true;
	}
	
	@Override
	public boolean refill(GumballMachine gumballMachine) {
		System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
		return false;
	}
}
