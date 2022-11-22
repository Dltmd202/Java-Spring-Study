/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * State Driven Transition (상태 중심 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 */
public class GumballMachine {
	private GumballState currentState;
	private int count = 0;	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = GumballState.NOCOINSTATE;
 		else currentState = GumballState.SOLDOUTSTATE;
	}
	public boolean isEmpty(){
		return (count==0);
	}
	public void changeState(GumballState nextState) {
		currentState = nextState;
	}
	public void insertCoin(){	
		currentState.insertCoin(this);
	}
	public void ejectCoin(){	
		currentState.ejectCoin(this);
	}
	public void turnCrank(){	
		currentState.turnCrank(this);
		currentState.dispense(this);
	}
	public void refill() {
		currentState.refill(this);
	}
	public void refill(int gumballs) {
		count = gumballs;
	}
	public int getNumberOfGumballs(){
		return count;
	}
	public void dispense(){
		if(count>0) --count;
	}
}
