`/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴: 상태 패턴을 사용하지 않은 구현
 * 문 (불필요한 System.out.println 문 제거 버전)
 */
public class SimpleDoor {
	private DoorState currentState = DoorState.CLOSED;
	public DoorState getState() {
		return currentState;
	}
	public void open(){
		if(currentState==DoorState.CLOSED)
			currentState = DoorState.OPENED;
	}
	public void close(){
		if(currentState==DoorState.OPENED)
			currentState = DoorState.CLOSED;
	}
	public void lock(){
		if(currentState==DoorState.CLOSED)
			currentState = DoorState.LOCKED;
	}
	public void unlock(){
		if(currentState==DoorState.LOCKED)
			currentState = DoorState.CLOSED;
	}
}
