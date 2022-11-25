public class GumballMachine {
    private static final GumballState soldOutState = new SoldOutState();
    private static final GumballState soldState = new SoldState();
    private static final GumballState noCoinState = new NoCoinState();
    private static final GumballState hasCoinState = new HasCoinState();

    private GumballState currentState;
    private int count = 0;

    public GumballMachine(int numberOfGumballs) {
        this.count = numberOfGumballs;
        if(count > 0) currentState = noCoinState;
        else currentState = soldOutState;
    }

    public void insertCoin(){
        if(currentState.insertCoin()) currentState = hasCoinState;
    }

    public void ejectCoin(){
        if(currentState.ejectCoin()) currentState = noCoinState;
    }

    public void turnCrank(){
        if(currentState.turnCrank()){
            currentState = soldState;
            if(currentState.dispense()){
                dispense();
                if(count == 0){
                    System.out.println("껌볼이 더 이상 없습니다.");
                    currentState = soldOutState;
                } else {
                    currentState = noCoinState;
                }
            }
        }
    }

    public void refill(){
        if(currentState.refill()){
            count = 20;
            currentState = noCoinState;
        }
    }

    public int getNumberOfGumballs(){
        return count;
    }

    private void dispense() {
        if(count > 0) --count;
        System.out.println(count);
    }
}
