package v3;

public class Main {
    public static void testCaffeineBeverage(CaffeineBeverage beverage){
        beverage.prepareRecipe();
    }

    public static void main(String[] args) {
        testCaffeineBeverage(new Coffee());
        testCaffeineBeverage(new Tea());
        testCaffeineBeverage(new CoffeeWithHook());
    }
}
