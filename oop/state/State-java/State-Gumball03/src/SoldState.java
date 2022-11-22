/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * SoldState.java
 * 상태 객체
 * @author 김상진
 */
public class SoldState implements GumballState {
	private GumballMachine gMachine;
	public SoldState(GumballMachine gMachine){
		this.gMachine = gMachine;
	}
	@Override
	public void insertCoin() {
		System.out.println("동전을 투입할 수 있는 단계가 아님");
	}

	@Override
	public void ejectCoin() {
		System.out.println("동전이 없음");
	}

	@Override
	public void turnCrank() {
		System.out.println("이미 손잡이를 돌렸음");
	}

	@Override
	public void dispense() {
		System.out.println("껌볼이 나옴");
		gMachine.dispense();
		if(gMachine.isEmpty()){
			System.out.println("껌볼이 더 이상 없습니다.");
			gMachine.changeToSoldOutState();
		}
		else{			
			gMachine.changeToNoCoinState();
		}
	}

	@Override
	public void refill() {
		System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
	}
}
