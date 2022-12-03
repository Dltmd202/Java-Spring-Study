package v7;

public class RubberDuck implements Quackable {
	private Observable observers = new Observable(this);
	@Override
	public void quack() {
		System.out.println("고무오리 >> 삑삑");
		notifyObservers();
	}

}
