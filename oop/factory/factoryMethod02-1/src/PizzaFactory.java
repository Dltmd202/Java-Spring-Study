public class PizzaFactory {

    public Pizza createPizza(String type){
        return switch (type) {
            case "cheese" -> new CheesePizza();
            case "potato" -> new PotatoPizza();
            default -> null;
        };
    }
}
