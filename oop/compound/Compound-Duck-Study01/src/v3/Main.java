package v3;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.simulate();
    }

    public void simulate(){
        Quackable quackable1 = new QuackCounter(new MallardDuck());
        simulate(quackable1);
        Quackable quackable2 = new QuackCounter(new RedheadDuck());
        simulate(quackable2);
        Quackable quackable3 = new QuackCounter(new DuckCall());
        simulate(quackable3);
        Quackable quackable4 = new QuackCounter(new RubberDuck());
        simulate(quackable4);
        Quackable quackable5 = new GooseAdapter(new Goose());
        simulate(quackable5);
        System.out.println("꽥꽥 수 :" + QuackCounter.getQuacks());
    }

    private void simulate(Quackable duck){
        duck.quack();
    }
}
