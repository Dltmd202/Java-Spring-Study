package turkey;

public class TurkeyTwoWayAdapter implements Turkey, Duck{
    private Turkey turkey;

    public TurkeyTwoWayAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    @Override
    public void quack() {
        turkey.gobble();
    }

    @Override
    public void gobble() {
        turkey.gobble();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 5; i++) {
            turkey.fly();
        }
    }
}
