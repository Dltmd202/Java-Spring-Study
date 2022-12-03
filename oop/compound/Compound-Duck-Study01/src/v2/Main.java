package v2;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.simulate();
    }

    public void simulate(){
        MallardDuck mallardDuck = new MallardDuck();
        simulate(mallardDuck);
        RedheadDuck redheadDuck = new RedheadDuck();
        simulate(mallardDuck);
        DuckCall duckCall = new DuckCall();
        simulate(duckCall);
        RubberDuck rubberDuck = new RubberDuck();
        simulate(rubberDuck);
    }

    public void simulate(Quackable duck){
        duck.quack();
    }
}
