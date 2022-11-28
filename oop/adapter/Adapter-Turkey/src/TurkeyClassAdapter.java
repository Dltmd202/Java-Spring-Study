public class TurkeyClassAdapter extends BlueSlateTurkey implements Duck{
    @Override
    public void quack() {
        gobble();
    }

    @Override
    public void fly() {
        for (int i = 0; i < 5; i++) {
            super.fly();
        }
    }
}
