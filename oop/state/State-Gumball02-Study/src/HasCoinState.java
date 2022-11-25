public class HasCoinState implements GumballState{
    private GumballMachine gumballMachine;

    public HasCoinState(GumballMachine gumballMachine){
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void ejectCoin() {
        System.out.println("취소되었음");
        gumballMachine.setState(gumballMachine.getNoCoinState());
    }

    @Override
    public void turnCrank() {
        System.out.println("손잡이를 돌렸음");
        gumballMachine.setState(gumballMachine.getSoldState());
    }

    @Override
    public void dispense() {
        System.out.println("손잡이를 돌려야 껌볼이 나옴");
    }

    @Override
    public void refill() {
        System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
    }
}
