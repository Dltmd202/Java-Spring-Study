package quack.impl;

import quack.QuackStrategy;

public class MuteQuack implements QuackStrategy {
    @Override
    public void doQuack() {
        System.out.println("...");
    }
}
