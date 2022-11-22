/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * 열린 상태
 */
public class Opened implements DoorState {
	private Door door;
    public Opened(Door door){
        this.door = door;
    }
	@Override
	public void open() {
		System.out.println("이미 열려 있음");
	}

	@Override
	public void close() {
		System.out.println("문이 닫힘");
		door.changeToClosedState();
	}

	@Override
	public void lock() {
		System.out.println("문이 열려 있어 잠글 수 없음");
	}

	@Override
	public void unlock() {
		System.out.println("문이 잠겨 있지 않음");
	}

}
