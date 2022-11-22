/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * State Driven Transition (상태 중심 전이)
 * 상태 객체에 문맥 전달 버전
 * 상태 객체 공유 가능 버전
 */
public class GumballMachine {
	private static final GumballState soldOutState = new SoldOutState();
	private static final GumballState soldState = new SoldState();
	private static final GumballState noCoinState = new NoCoinState();
	private static final GumballState hasCoinState = new HasCoinState();
	private GumballState currentState;
	private int count = 0;
	
	void changeToSoldOutState(){
		currentState = soldOutState;
	}
	void changeToSoldState(){
		currentState = soldState;
	}
	void changeToNoCoinState(){
		currentState = noCoinState;
	}
	void changeToHasCoinState() {
		currentState = hasCoinState;
	}
	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = noCoinState;
 		else currentState = soldOutState;
	}	
	public void insertCoin(){	
		currentState.insertCoin(this);
	}
	public void ejectCoin(){	
		currentState.ejectCoin(this);
	}
	public void turnCrank(){	
		currentState.turnCrank(this);
		// 손잡이를 돌린 후에는 무조건 껌볼 판매 상태 실행. 분리해서 구현 불필요
		currentState.dispense(this); 
	}
	public void refill(){	
		currentState.refill(this);
	}
	
	public int getNumberOfGumballs(){
		return count;
	}
	void dispense(){
		if(count>0) --count;
		System.out.println(count);
	}
	void refill(int gumballs) {
		count = gumballs;
	}
	
	public boolean isEmpty(){
		return (count==0);
	}
}
