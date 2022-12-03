package v5;

import java.util.ArrayList;
import java.util.List;

public class Flock implements Quackable{
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
}
