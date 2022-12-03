import java.util.ArrayList;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * QuackObservable를 구현하는 모든 클래스는 
 * registerObserver, notifyObserver 등을 구현해야 함
 * 공통 코드이기 때문에 이 클래스에서 구현하고 나머지 클래스들은 이 클래스를 활용하도록 함
 * 오리를 생성자에서 인자로 받는 이유는 notifyObservers를 구현할 때 필요하기 때문
 */
public class Observable implements QuackObservable {
	private ArrayList<Observer> observers = new ArrayList<>();
	private QuackObservable duck;
	public Observable(QuackObservable duck){
		this.duck = duck;
	}
	@Override
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	@Override
	public void notifyObservers(){
		for(Observer o: observers)
			o.update(duck);
	}
}
