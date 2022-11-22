/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * 문맥 중심 전이
 * 싱글톤을 이용한 상태 객체 공유
 */
public class GumballMachine {
	private GumballState currentState;
	private int count = 0;
	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = NoCoinState.getInstance();
 		else currentState = SoldOutState.getInstance();
	}	
	public void insertCoin(){	
		if(currentState.insertCoin()) currentState = HasCoinState.getInstance();
	}
	public void ejectCoin(){	
		if(currentState.ejectCoin()) currentState = NoCoinState.getInstance();
	}
	public void turnCrank(){	
		if(currentState.turnCrank()) {
			currentState = SoldState.getInstance();
			if(currentState.dispense()){
				dispense();
				if(count==0){
					System.out.println("껌볼이 더 이상 없습니다.");
					currentState = SoldOutState.getInstance();
				}
				else{			
					currentState = NoCoinState.getInstance();
				}
			}
		}
	}
	public void refill() {
		if(currentState.refill()) {
			count = 20;
			currentState = NoCoinState.getInstance();
		}
	}
	public int getNumberOfGumballs(){
		return count;
	}
	public void dispense(){
		if(count>0) --count;
		System.out.println(count);
	}
}
