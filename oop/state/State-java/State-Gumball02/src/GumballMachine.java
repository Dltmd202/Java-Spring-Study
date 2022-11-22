/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * State Driven Transition (상태 중심 전이)
 * 상태 객체에 문맥 전달 버전
 * 상태 전이를 위해 상태 객체를 전달하는 getter가 상태 수만큼 정의되어야 함 + 한 개의 setter
 * >> 다른 방법: 상태 수만큼 상태를 전이하는 메소드 정의
 */
public class GumballMachine {
	private final GumballState soldOutState = new SoldOutState(this);
	private final GumballState soldState = new SoldState(this);
	private final GumballState noCoinState = new NoCoinState(this);
	private final GumballState hasCoinState = new HasCoinState(this);
	private GumballState currentState;
	private int count = 0;
	GumballState getSoldOutState() {
		return soldOutState;
	}
	GumballState getSoldState() {
		return soldState;
	}
	GumballState getNoCoinState() {
		return noCoinState;
	}
	GumballState getHasCoinState() {
		return hasCoinState;
	}
	void setState(GumballState nextState){
		currentState = nextState;
	}
	/*
	// 이와 같은 메소드를 사용할 경우 상태 수만큼 필요
	// set(get()) 형태보다 효과적임
	public void changeToNoCoinState() {
		currentState = noCoinState;
	}
	*/
	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = noCoinState;
 		else currentState = soldOutState;
	}	
	public void insertCoin(){	
		currentState.insertCoin();
		// currentState.insertCoin(this);
	}
	public void ejectCoin(){	
		currentState.ejectCoin();
	}
	public void turnCrank(){	
		currentState.turnCrank();
		// 손잡이를 돌린 후에는 무조건 껌볼 판매 상태 실행. 분리해서 구현 불필요
		currentState.dispense(); 
	}
	public void refill(){	
		currentState.refill();
	}
	public void refill(int gumballs) {
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
