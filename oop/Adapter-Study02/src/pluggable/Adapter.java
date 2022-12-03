package pluggable;

public class Adapter implements Target{
    private Target target;

    public Adapter(Target target) {
        this.target = target;
    }

    @Override
    public void foo() {
       target.foo();
    }
}
