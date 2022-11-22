/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * 열린 상태
 */
public class Opened implements DoorState {
	@Override
	public boolean open() {
		System.out.println("이미 열려 있음");
		return false;
	}

	@Override
	public boolean close() {
		System.out.println("문이 닫힘");
		return true;
	}

	@Override
	public boolean lock() {
		System.out.println("문이 열려 있어 잠글 수 없음");
		return false;
	}

	@Override
	public boolean unlock() {
		System.out.println("문이 잠겨 있지 않음");
		return false;
	}

}
