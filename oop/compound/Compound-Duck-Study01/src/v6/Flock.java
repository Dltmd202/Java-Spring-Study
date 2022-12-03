package v6;

import java.util.ArrayList;
import java.util.List;

public class Flock implements Quackable {
    private List<Quackable> quackables = new ArrayList<>();
    public void add(Quackable duck){
        quackables.add(duck);
    }

    @Override
    public void quack() {
        for (Quackable quackable : quackables) {
            quackable.quack();
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        for (Quackable duck : quackables) {
            System.out.println(duck);
            duck.registerObserver(observer);
        }
    }
}
