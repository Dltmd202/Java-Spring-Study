public class Main {
    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore(new PizzaFactory());
        pizzaStore.orderPizza("치즈");
        pizzaStore.orderPizza("포테이토");
    }
}