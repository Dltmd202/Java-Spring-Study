/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * Context Driven Transition (문맥 중심 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 * 상태 객체의 메소드가 다음 상태를 반환 
 * 전이 메소드에 문맥 객체를 인자로 전달함 
 * 상태 중심 전이와 큰 차이가 없음
 */
public class GumballMachine {
	private GumballState currentState;
	private int gumballs = 0;	
	public GumballMachine(int numberGumballs) {
		gumballs = numberGumballs;
 		if(gumballs > 0) currentState = GumballState.NOCOINSTATE;
 		else currentState = GumballState.SOLDOUTSTATE;
	}	
	public void insertCoin(){	
		currentState = currentState.insertCoin(this);
	}
	public void ejectCoin(){	
		currentState = currentState.ejectCoin(this);
	}
	public void turnCrank(){	
		currentState = currentState.turnCrank(this);
		currentState = currentState.dispense(this);
	}
	public void refill(){ 
		currentState = currentState.refill(this);
	}
	public int getNumberOfGumballs(){
		return gumballs;
	}
	void dispense(){
		if(gumballs>0) --gumballs;
		System.out.println("현재 컴볼 수: "+gumballs);
	}
	void refill(int gumballs) {
		this.gumballs = gumballs;
	}
}
