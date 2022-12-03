package v2;

public class HasCoinState implements GumballState{
    private GumballMachine gumballMachine;

    public HasCoinState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void ejectCoin() {
        System.out.println("이미 동전이 있음");
    }

    @Override
    public void turnCrank() {
        System.out.println("취소되었음");

    }

    @Override
    public void dispense() {

    }

    @Override
    public void refill() {

    }
}
