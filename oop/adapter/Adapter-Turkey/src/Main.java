public class Main {
    public static void turkeyTest(Turkey turkey) {
        turkey.gobble();
        turkey.fly();
    }
    public static void duckTest(Duck duck) {
        duck.quack();
        duck.fly();
    }

    public static void main(String[] args) {
        MallardDuck duck = new MallardDuck();
        BlueSlateTurkey bsTurkey = new BlueSlateTurkey();
        BlackTurkey bTurkey = new BlackTurkey();
        TurkeyObjectAdapter turkeyObjectAdapter1 = new TurkeyObjectAdapter(bsTurkey);
        TurkeyObjectAdapter turkeyObjectAdapter2 = new TurkeyObjectAdapter(bTurkey);
        TurkeyClassAdapter turkeyClassAdapter = new TurkeyClassAdapter();
        TurkeyTwoWayAdapter turkey2WayAdapter = new TurkeyTwoWayAdapter(bsTurkey);

        System.out.println("오리 테스트");
        duckTest(duck);
        duckTest(turkeyObjectAdapter1);
        duckTest(turkeyObjectAdapter2);
        duckTest(turkeyClassAdapter);
        duckTest(turkey2WayAdapter);

        System.out.println("\n칠면조 테스트");
        turkeyTest(bsTurkey);
        turkeyTest(turkeyClassAdapter);
        turkeyTest(turkey2WayAdapter);
    }
}