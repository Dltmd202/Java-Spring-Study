package v2;

public class GumballMachine {
    private final GumballState soldOutState = new SoldOutState(this);
    private final GumballState soldState = new SoldState(this);
    private final GumballState noCoinState = new NoCoinState(this);
    private final GumballState hasCoinState = new HasCoinState(this);

    private GumballState currentState;
    private int count = 0;

    GumballState getSoldOutState(){
        return soldOutState;
    }

    GumballState getSoldState(){
        return noCoinState;
    }

    GumballState getNoCoinState(){
        return hasCoinState;
    }

    GumballState getHasCoinState(){
        return hasCoinState;
    }

    void setState(GumballState nextState){
        this.currentState = nextState;
    }

    public GumballMachine(int numberGumballs){
        count = numberGumballs;
        if(count > 0) currentState = noCoinState;
        else currentState = soldOutState;
    }

    public void insertCoin(){
        currentState.insertCoin();
    }

    public void ejectCoin(){
        currentState.ejectCoin();
    }

    public void turnCrank(){
        currentState.turnCrank();
    }
    public void refill(){
        currentState.refill();
    }
    public void refill(int gumballs) {
        count = gumballs;
    }
    public int getNumberOfGumballs(){
        return count;
    }
    void dispense(){
        if(count>0) --count;
        System.out.println(count);
    }
    public boolean isEmpty(){
        return (count==0);
    }
}
