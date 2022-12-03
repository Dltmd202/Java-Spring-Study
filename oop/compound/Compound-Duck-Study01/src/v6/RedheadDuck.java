package v6;

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
