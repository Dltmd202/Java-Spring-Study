/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * 상태 interface
 */
public interface DoorState {
	boolean open();
	boolean close();
	boolean lock();
	boolean unlock();
	// default boolean open() { return false; }
}
