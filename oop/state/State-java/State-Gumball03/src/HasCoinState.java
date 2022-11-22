import java.util.concurrent.ThreadLocalRandom;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2022년도 2학기
 * 상태 패턴
 * HasCoinState.java
 * 상태 객체
 * @author 김상진
 */
public class HasCoinState implements GumballState {
	private GumballMachine gMachine;
	public HasCoinState(GumballMachine gMachine){
		this.gMachine = gMachine;
	}
	
	@Override
	public void insertCoin() {
		System.out.println("이미 동전이 있음");

	}

	@Override
	public void ejectCoin() {
		System.out.println("취소되었음");
		gMachine.changeToNoCoinState();
	}

	@Override
	public void turnCrank() {
		System.out.println("손잡이를 돌렸음");
		// changed
		if (ThreadLocalRandom.current().nextDouble()<0.5) { //노트와 달리 50%
			gMachine.changeToDoubleSoldState();
		} else {
			gMachine.changeToSoldState();
		}
	}

	@Override
	public void dispense() {
		System.out.println("손잡이를 돌려야 껌볼이 나옴");
	}
	
	@Override
	public void refill() {
		System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
	}
}
