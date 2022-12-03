package v7;

public class RedheadDuck implements Quackable {
	private Observable observers = new Observable(this);
	@Override
	public void quack() {
		System.out.println("붉은머리오리 >> 꽥꽥");
		notifyObservers();
	}
}
