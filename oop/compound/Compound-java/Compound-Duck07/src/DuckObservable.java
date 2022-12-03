import java.util.ArrayList;
import java.util.List;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * QuackObservable를 구현하는 모든 클래스가 이 클래스를 has-a로 유지하는 대신
 * 장식 패턴을 이용함
 */
public class DuckObservable implements Quackable {
	private List<Observer> observers = new ArrayList<>();
	private Quackable duck;
	public DuckObservable(Quackable duck){
		this.duck = duck;
	}
	@Override
	public void quack() {
		duck.quack();
		notifyObservers();
	}
	@Override
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	@Override
	public void notifyObservers(){
		for(var o: observers) o.update(duck);
	}
}
