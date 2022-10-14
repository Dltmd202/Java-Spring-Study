package fly.impl;

import fly.FlyStrategy;

public class FlyNoWay implements FlyStrategy {
    @Override
    public void doFly() {
        System.out.println("no fly~~~");
    }
}
