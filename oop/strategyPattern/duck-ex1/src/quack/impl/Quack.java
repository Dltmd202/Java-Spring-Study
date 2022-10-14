package quack.impl;

import quack.QuackStrategy;

public class Quack implements QuackStrategy {
    @Override
    public void doQuack() {
        System.out.println("quack quack~~");
    }
}
