/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * 잠김 상태
 */
public class Locked implements DoorState {
	@Override
	public boolean open() {
		System.out.println("문이 잠겨 있어 열 수 없음");
		return false;
	}

	@Override
	public boolean close() {
		System.out.println("문이 이미 닫혀 있음");
		return false;
	}

	@Override
	public boolean lock() {
		System.out.println("문이 이미 잠겨 있음");
		return false;
	}

	@Override
	public boolean unlock() {
		System.out.println("문의 잠금을 해제함");
		return true;
	}

}
