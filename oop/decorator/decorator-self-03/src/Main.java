public class Main {
    public static void main(String[] args) {
        Beverage beverage =
                new Mocha(
                        new Whip(
                                new Mocha(
                                        new HouseBlend()
                                )
                        )
                );

        System.out.println(beverage.getDescription() + " " + beverage.cost());

        beverage = new Milk(new Mocha(new DarkRost()));

        System.out.println(beverage.getDescription() + " " + beverage.cost());
    }
}