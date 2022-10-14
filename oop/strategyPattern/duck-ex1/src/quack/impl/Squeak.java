package quack.impl;

import quack.QuackStrategy;

public class Squeak implements QuackStrategy {
    @Override
    public void doQuack() {
        System.out.println("squeak squeak~~~");
    }
}
