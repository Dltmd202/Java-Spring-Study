public class GumballMachine{
    private final GumballState soldOutState = new SoldOutState(this);
    private final GumballState soldState = new SoldState(this);
    private final GumballState noCoinState = new NoCoinState(this);
    private final GumballState hasCoinState = new HasCoinState(this);
    private final GumballState doubleSoldState = new DoubleSoldState(this);

    private GumballState currentSate;
    private int count = 0;

    void changeToSoldOutState(){
        currentSate = soldOutState;
    }
    void changeToSoldState(){
        currentSate = soldState;
    }
    void changeToNoCoinState(){
        currentSate = noCoinState;
    }
    void changeToHasCoinState(){
        currentSate = hasCoinState;
    }
    void changeToDoubleSoldState(){
        currentSate = doubleSoldState;
    }

    public GumballMachine(int numberGumballs){
        count = numberGumballs;
        if(count > 0) currentSate = noCoinState;
        else currentSate = soldState;
    }

    public void insertCoin(){
        currentSate.insertCoin();
    }

    public void ejectCoin(){
        currentSate.ejectCoin();
    }

    public void turnCrank(){
        currentSate.turnCrank();
        currentSate.dispense();
    }

    public void refill(){
        currentSate.refill();
    }

    public void refill(int gumballs){
        count = gumballs;
    }

    public int getNumberOfGumballs(){
        return count;
    }

    public void dispense(){
        if(count > 0) --count;
        System.out.println(count);
    }

    public boolean isEmpty(){
        return count == 0;
    }


}
