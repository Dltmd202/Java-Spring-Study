package duck;

import duck.behavior.fly.FlyBehavior;
import duck.behavior.quock.QuackBehavior;

public class DuckDuck extends Duck {

    public DuckDuck() {
        quackBehavior = new QuackBehavior() {
            @Override
            public void quack() {
                System.out.println("덕덕");
            }
        };
        flyBehavior = new FlyBehavior() {
            @Override
            public void fly() {
                System.out.println("플라이 플라이~");
            }
        };
    }

    @Override
    public void display() {

    }
}
