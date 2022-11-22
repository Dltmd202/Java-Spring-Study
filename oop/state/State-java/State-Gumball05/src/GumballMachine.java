import java.util.concurrent.ThreadLocalRandom;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2021년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * Context driven transition (문맥 중심 전이) 
 * DoubleSold 상태 추가 버전
 */

public class GumballMachine {
	private static final GumballState soldOutState = new SoldOutState();
	private static final GumballState soldState = new SoldState();
	private static final GumballState noCoinState = new NoCoinState();
	private static final GumballState hasCoinState = new HasCoinState();
	// added
	private static final GumballState doubleSoldState = new DoubleSoldState();
	
	private GumballState currentState;
	private int count = 0;
	
	public GumballMachine(int numberGumballs) {
		count = numberGumballs;
 		if(count > 0) currentState = noCoinState;
 		else currentState = soldOutState;
	}	
	public void insertCoin(){	
		if(currentState.insertCoin()) currentState = hasCoinState;
	}
	public void ejectCoin(){	
		if(currentState.ejectCoin()) currentState = noCoinState;
	}
	public void turnCrank(){	
		// changed
		if(currentState.turnCrank()){
			int winner = ThreadLocalRandom.current().nextInt(10);
			if (winner<5) currentState = doubleSoldState;
			else currentState = soldState;	
			if(currentState.dispense()){
				dispense();
				if(count>=1&&currentState==doubleSoldState)
					dispense();
				if(count==0){
					System.out.println("껌볼이 더 이상 없습니다.");
					currentState = soldOutState;
				}
				else currentState = noCoinState;
			}
		}
	}	
	public void refill() {
		if(currentState.refill()) {
			count = 20;
			currentState = noCoinState;
		}
	}
	public int getNumberOfGumballs(){
		return count;
	}
	public void dispense(){
		System.out.println(count);
		if(count>0) --count;
	}
}
