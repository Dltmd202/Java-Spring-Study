/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴: 상태 기반 전이 방식
 * 문
 */
public class Door {
	private final DoorState openedState = new Opened(this);
	private final DoorState closedState = new Closed(this);
	private final DoorState lockedState = new Locked(this);
	private DoorState currentState = closedState;
	
	public DoorState getState() {
		return currentState;
	}
	void changeToOpenedState(){
		currentState = openedState;
	}
	void changeToClosedState(){
		currentState = closedState;
	}
	void changeToLockedState(){
		currentState = lockedState;
	}
	public void open(){
		currentState.open();
	}
	public void close(){
		currentState.close();
	}
	public void lock(){
		currentState.lock();
	}
	public void unlock(){
		currentState.unlock();
	}
}
