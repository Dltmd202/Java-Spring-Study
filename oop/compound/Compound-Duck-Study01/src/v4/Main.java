package v4;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        AbstractDuckFactory duckFactory = new CountingDuckFactory();
        main.simulate(duckFactory);
    }

    public void simulate(AbstractDuckFactory duckFactory){
        Quackable mallardDuck = duckFactory.createMallardDuck();
        simulate(mallardDuck);
        Quackable redheadDuck = duckFactory.createRedheadDuck();
        simulate(redheadDuck);
        Quackable duckCall = duckFactory.createDuckCall();
        simulate(duckCall);
        Quackable rubberDuck = duckFactory.createRubberDuck();
        simulate(rubberDuck);
        Quackable goose = new GooseAdapter(new Goose());
        simulate(goose);
        System.out.println("꽥꽥 수 :" + QuackCounter.getQuacks());
    }

    private void simulate(Quackable duck){
        duck.quack();
    }
}
