/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * Context driven transition (문맥 중심 전이) 
 * 장점. 상태 객체에 문맥을 전달할 필요가 없음
 * 장점. 상태 객체를 공유할 수 있음
 * 장점. 상태를 전이하기 위한 getter와 setter가 필요 없음
 * 단점. 문맥 객체는 상태 중심 전이 방식보다 복잡해짐 (조건문도 여전히 남아 있음)
 * 단점. 원래 상태 패턴은 특정 상태에서 문맥이 해야 할 일을 상태 객체에 위임해야 하는데 위임하지 않고 있음
 *      상태 객체가 하는 일이 상태 전이를 해야하는지 여부만 알려주고 있음
 * 이 버전은 상태 객체에서 상태 전이가 필요한지 여부를 boolean 값을 통해 알려줌
 */
public class GumballMachine {
	// 상태 중심과 비교
	// private final GumballState soldOutState = new SoldOutState(this);
	private static final GumballState soldOutState = new SoldOutState();
	private static final GumballState soldState = new SoldState();
	private static final GumballState noCoinState = new NoCoinState();
	private static final GumballState hasCoinState = new HasCoinState();
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
		if(currentState.turnCrank()) {
			currentState = soldState;
			if(currentState.dispense()){
				dispense();
				if(count==0){
					System.out.println("껌볼이 더 이상 없습니다.");
					currentState = soldOutState;
				}
				else{			
					currentState = noCoinState;
				}
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
		if(count>0) --count;
		System.out.println(count);
	}
}
