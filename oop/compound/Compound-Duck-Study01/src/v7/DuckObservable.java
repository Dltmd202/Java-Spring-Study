package v7;

import java.util.ArrayList;
import java.util.List;

public class DuckObservable implements Quackable{
    private List<Observer> observers = new ArrayList<>();
    private Quackable duck;

    public DuckObservable(Quackable duck) {
        this.duck = duck;
    }

    @Override
    public void quack() {
        duck.quack();
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(duck);
        }
    }
}
