/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴: 문맥 기반 전이 방식
 * 문
 */
public class Door {
	private static final DoorState openedState = new Opened();
	private static final DoorState closedState = new Closed();
	private static final DoorState lockedState = new Locked();
	private DoorState currentState = closedState;
	public DoorState getState() {
		return currentState;
	}
	public void open(){
		if(currentState.open()) currentState = openedState;
		// currentState = currentState.open();
	}
	public void close(){
		if(currentState.close()) currentState = closedState;
	}
	public void lock(){
		if(currentState.lock()) currentState = lockedState;
	}
	public void unlock(){
		if(currentState.unlock()) currentState = closedState;
	}
}
