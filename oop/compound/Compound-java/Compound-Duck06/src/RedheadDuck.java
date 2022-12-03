/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 복합 패턴
 * 붉은머리오리
 * 관찰자 패턴에서 Subject 기능 추가 (코드 중복)
 * 해당 기능 추가를 좀 더 편리하게 하기 위해 Observable를 이용함
 * 개별 기능을 Observable에 구현되고 MallardDuck은 그쪽으로 위임함
 * 그래도 코드 중복
 *
 */
public class RedheadDuck implements Quackable {
	private Observable observers = new Observable(this);
	@Override
	public void quack() {
		System.out.println("붉은머리오리 >> 꽥꽥");
		notifyObservers();
	}
	@Override
	public void registerObserver(Observer observer) {
		observers.registerObserver(observer);
	}
	@Override
	public void notifyObservers() {
		observers.notifyObservers();
	}
}
