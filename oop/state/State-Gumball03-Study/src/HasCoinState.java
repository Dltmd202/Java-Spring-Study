import java.util.concurrent.ThreadLocalRandom;

public class HasCoinState implements GumballState{
    private GumballMachine gumballMachine;

    public HasCoinState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertCoin() {
        System.out.println("이미 동전이 있음");
    }

    @Override
    public void ejectCoin() {
        System.out.println("취소되었음.");
    }

    @Override
    public void turnCrank() {
        System.out.println("손잡이를 돌렸음");
        if(ThreadLocalRandom.current().nextDouble() < 0.5){
        }
    }

    @Override
    public void dispense() {

    }

    @Override
    public void refill() {

    }
}
