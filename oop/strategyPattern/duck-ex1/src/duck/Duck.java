package duck;

import fly.FlyStrategy;
import quack.QuackStrategy;

public abstract class Duck {
    private FlyStrategy flyStrategy;
    private QuackStrategy quackStrategy;

    public void quack(){
        quackStrategy.doQuack();
    }

    public void swim(){
        System.out.println("swimming~~");
    }

    public void fly(){
        flyStrategy.doFly();
    }

    public void setFlyStrategy(FlyStrategy flyStrategy) {
        this.flyStrategy = flyStrategy;
    }

    public void setQuackStrategy(QuackStrategy quackStrategy) {
        this.quackStrategy = quackStrategy;
    }

    public abstract void display();
}
