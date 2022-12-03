package v1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public void simulate(){
        Quackable mallardDuck = new MallardDuck();
        simulate(mallardDuck);
        Quackable readheadDuck = new ReadheadDuck();
        simulate(readheadDuck);
        Quackable duckCall = new DuckCall();
        simulate(duckCall);
        RubberDuck rubberDuck = new RubberDuck();
        simulate(rubberDuck);
    }

    private void simulate(Quackable duck){
        duck.quack();
    }
}