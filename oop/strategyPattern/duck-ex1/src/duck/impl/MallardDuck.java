package duck.impl;

import duck.Duck;
import fly.impl.FlyWithWings;
import quack.impl.Quack;

public class MallardDuck extends Duck {

    public MallardDuck(){
        setFlyStrategy(new FlyWithWings());
        setQuackStrategy(new Quack());
    }

    @Override
    public void display() {
        System.out.println();
    }
}
