/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 2021년도 2학기
 * 상태 패턴
 * HasCoinState.java
 * 상태 객체
 * @author 김상진
 */
public class HasCoinState implements GumballState {
	@Override
	public boolean insertCoin() {
		System.out.println("이미 동전이 있음");
		return false;
	}

	@Override
	public boolean ejectCoin() {
		System.out.println("취소되었음");
		return true;
	}

	@Override
	public boolean turnCrank() {
		System.out.println("손잡이를 돌렸음");
		return true;
	}

	@Override
	public boolean dispense() {
		System.out.println("손잡이를 돌려야 껌볼이 나옴");
		return false;
	}
	
	@Override
	public boolean refill() {
		System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
		return false;
	}
}