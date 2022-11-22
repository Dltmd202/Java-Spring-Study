/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * Context Driven Transition (문맥 기반 전이)
 * 열거형으로 상태 객체들을 정의. 한 자바 파일에 모든 상태 구현.
 * 상태 객체의 메소드 true/false를 반환 
 *
 */
public class GumballMachine {
	private GumballState currentState;
	private int count = 0;	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = GumballState.NOCOINSTATE;
 		else currentState = GumballState.SOLDOUTSTATE;
	}	
	public void insertCoin(){	
		if(currentState.insertCoin()) currentState = GumballState.HASCOINSTATE;
	}
	public void ejectCoin(){	
		if(currentState.ejectCoin()) currentState = GumballState.NOCOINSTATE;
	}
	public void turnCrank(){	
		if(currentState.turnCrank()) currentState = GumballState.SOLDSTATE;
		if(currentState.dispense()){
			dispense();
			if(count==0){
				System.out.println("껌볼이 더 이상 없습니다.");
				currentState = GumballState.SOLDOUTSTATE;
			}
			else{			
				currentState = GumballState.NOCOINSTATE;
			}
		}
	}
	public void refill() {
		if(currentState.refill()) {
			count = 20;
			currentState = GumballState.NOCOINSTATE;
		}
	}
	public int getNumberOfGumballs(){
		return count;
	}
	public void dispense(){
		if(count>0) --count;
	//	System.out.println(count);
	}
}
