package v7;

public class CountingObservableDuckFactory extends CountingDuckFactory{
    @Override
    public Quackable createMallardDuck() {
        return new DuckObservable(super.createMallardDuck());
    }

    @Override
    public Quackable createRedheadDuck() {
        return new DuckObservable(super.createRedheadDuck());
    }

    @Override
    public Quackable createDuckCall() {
        return new DuckObservable(super.createDuckCall());
    }

    @Override
    public Quackable createRubberDuck() {
        return new DuckObservable(super.createRubberDuck());
    }
}
