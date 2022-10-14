public class PizzaStore {

    public Pizza orderPizza(String type){
        Pizza pizza = null;

        switch (type) {
            case "cheese":
                pizza = new CheesePizza();
                break;
            case "potato":
                pizza = new PotatoPizza();
                break;
        }

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }
}
