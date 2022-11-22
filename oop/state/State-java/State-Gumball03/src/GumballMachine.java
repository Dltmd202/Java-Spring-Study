/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * State Driven Transition (상태 중심 전이)
 * 상태 객체에 문맥 전달 버전
 * DoubleSold 상태 추가 버전
 * @author 김상진
 *
 */
public class GumballMachine {
	private final GumballState soldOutState = new SoldOutState(this);
	private final GumballState soldState = new SoldState(this);
	private final GumballState noCoinState = new NoCoinState(this);
	private final GumballState hasCoinState = new HasCoinState(this);
	// added
	private final GumballState doubleSoldState = new DoubleSoldState(this);
	
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
	// added
	void changeToDoubleSoldState() {
		currentState = doubleSoldState;
	}
	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = noCoinState;
 		else currentState = soldOutState;
	}	
	public void insertCoin(){	
		currentState.insertCoin();
	}
	public void ejectCoin(){	
		currentState.ejectCoin();
	}
	public void turnCrank(){	
		currentState.turnCrank();
		currentState.dispense();
	}
	public void refill(){	
		currentState.refill();
	}
	void refill(int gumballs) {
		count = gumballs;
	}
	public int getNumberOfGumballs(){
		return count;
	}
	void dispense(){
		if(count>0) --count;
		System.out.println(count);  
	}
	public boolean isEmpty(){
		return (count==0);
	}
}
