package v5;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        AbstractDuckFactory duckFactory = new CountingDuckFactory();
        main.simulate(duckFactory);
    }

    public void simulate(AbstractDuckFactory duckFactory){
        Quackable mallardDuck = duckFactory.createMallardDuck();

        Flock flockOfDucks = new Flock();
        flockOfDucks.add(mallardDuck);
        Flock flockOfRedheads = new Flock();
        flockOfRedheads.add(duckFactory.createRedheadDuck());
        flockOfRedheads.add(duckFactory.createRedheadDuck());
        flockOfRedheads.add(flockOfRedheads);
        simulate(flockOfDucks);

        Quackable duckCall = duckFactory.createDuckCall();
        simulate(duckCall);
        Quackable rubberDuck = duckFactory.createRubberDuck();
        simulate(rubberDuck);
        Quackable goose = new GooseAdapter(new Goose());
        simulate(goose);
        System.out.printf("꽥꽥 수: %d%n", QuackCounter.getQuacks());
    }

    private void simulate(Quackable duck){
        duck.quack();
    }
}
