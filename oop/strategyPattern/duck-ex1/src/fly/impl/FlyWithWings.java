package fly.impl;

import fly.FlyStrategy;

public class FlyWithWings implements FlyStrategy {
    @Override
    public void doFly() {
        System.out.println("wing wing~~");
    }
}
