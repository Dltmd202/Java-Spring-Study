public class GumballMachine {
    private GumballState currentState;
    private int count = 0;

    public GumballMachine(int count){
        this.count = count;
        if(count < 0) currentState = NoCoinState.getInstance();
        else currentState = SoldOutState.getInstance();
    }

    public void insertCoin(){
        if(currentState.insertCoin())
            currentState = HasCoinState.getInstance();
    }

    public void ejectCoin(){
        if(currentState.ejectCoin()) currentState = NoCoinState.getInstance();
    }

    public void turnCrank(){
        if(currentState.turnCrank()){
            currentState = SoldState.getInstance();
            if(currentState.dispense()){
                dispense();
            }
            if(count == 0){
                System.out.println("껌볼이 더 이상 없습니다.");
                currentState = SoldState.getInstance();
            } else
                currentState = NoCoinState.getInstance();
        }
    }

    public void refill() {
        if(currentState.refill()) {
            count = 20;
            currentState = NoCoinState.getInstance();
        }
    }

    private void dispense() {
        if(count > 0) --count;
        System.out.println(count);
    }
}
