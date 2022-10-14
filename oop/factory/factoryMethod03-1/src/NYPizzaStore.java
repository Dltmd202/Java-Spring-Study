public class NYPizzaStore extends PizzaStore{
    @Override
    protected Pizza createPizza(String type) {
        Pizza pizza = null;

        switch (type){
            case "cheese": {
                pizza = new NYCheesePizza();
                break;
            }
            case "potato":{
                pizza = new NYPotatoPizza();
                break;
            }
        }
        return pizza;
    }
}
