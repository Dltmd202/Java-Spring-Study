/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴: 상태 패턴을 사용하지 않는 버전
 * 문
 */
public class Door {
	private DoorState currentState = DoorState.CLOSED;
	public DoorState getState() {
		return currentState;
	}
	public void open(){
		switch(currentState){
		case OPENED:
			System.out.println("이미 열려 있음");
			break;
		case CLOSED:
			System.out.println("문이 열림");
			currentState = DoorState.OPENED;
			break;
		case LOCKED:
			System.out.println("문이 잠겨 있어 열 수 없음");
			break;
		} // switch
	}
	public void close(){
		switch(currentState){
		case OPENED:
			System.out.println("문이 닫힘");
			currentState = DoorState.CLOSED;
			break;
		case CLOSED, LOCKED:
			System.out.println("문이 이미 닫혀 있음");
			break;
		} // switch
	}
	public void lock(){
		switch(currentState){
		case OPENED:
			System.out.println("문이 열려 있어 잠글 수 없음");
			break;
		case CLOSED:
			System.out.println("문을 잠금");
			currentState = DoorState.LOCKED;
			break;
		case LOCKED:
			System.out.println("문이 이미 잠겨 있음");
			break;
		} // switch
	}
	public void unlock(){
		switch(currentState){
		case OPENED, CLOSED:
			System.out.println("문이 잠겨 있지 않음");
			break;
		case LOCKED:
			System.out.println("문의 잠금을 해제함");
			currentState = DoorState.CLOSED;
			break;
		} // switch
	}
}
