import java.util.concurrent.ThreadLocalRandom;

public class GumballMachine {
    private static final GumballState soldOutState = new SoldOutState();
    private static final GumballState soldState = new SoldState();
    private static final GumballState noCoinState = new NoCoinState();
    private static final GumballState hasCoinState = new HasCoinState();
    // added
    private static final GumballState doubleSoldState = new DoubleSoldState();
    private GumballState currentState;

    private int counnt = 0;

    public GumballMachine(int counnt) {
        this.counnt = counnt;
        if(counnt > 0) currentState = noCoinState;
        else currentState = soldOutState;
    }

    public void insertCoin(){
        if(currentState.insertCoin())
            currentState = hasCoinState;
    }

    public void ejectCoin(){
        if(currentState.ejectCoin())
            currentState = noCoinState;
    }

    public void turnCrank(){
        if(currentState.turnCrank()){
            int winner = ThreadLocalRandom.current().nextInt(10);
            if(winner < 5) currentState = doubleSoldState;
            else currentState = soldState;
            if(currentState.dispense()){
                dispense();
                if(counnt >=1 && currentState == doubleSoldState)
                    dispense();
                if(counnt == 0) {
                    System.out.println("껌볼이 더 이상 없습니다.");
                    currentState = soldOutState;
                }
                else currentState = noCoinState;
            }
        }
    }

    public void refill(){
        if(currentState.refill()) {
            counnt = 20;
            currentState = noCoinState;
        }
    }

    public void dispense(){
        System.out.println(counnt);
        if(counnt > 0) -- counnt;
    }
}
