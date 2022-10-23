public class Mocha extends CondimentDecorator{

    protected Mocha(Beverage beverage) {
        super(beverage);
    }

    @Override
    public int cost() {
        return beverage.cost() + 200;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 모카";
    }
}
