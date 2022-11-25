public class GumballMachine {
    private GumballState currentState;

    private int count = 0;

    public GumballMachine(int numberGumballs) {
        count = numberGumballs;
        if(count > 0) currentState = GumballState.NOCOINSTATE;
        else currentState = GumballState.SOLDOUTSTATE;
    }

    public void changeState(GumballState state) {
        this.currentState = state;
    }

    public void insertCoin(){
        currentState.insertCoin(this);
    }
    public void ejectCoin(){
        currentState.ejectCoin(this);
    }

    public void turnCrank(){
        currentState.turnCrank(this);
        currentState.dispense(this);
    }
    public void refill() {
        currentState.refill(this);
    }

    public void dispense(){
        if(count>0) --count;
    }


    public boolean isEmpty() {
        return count == 0;
    }

    public void refill(int gumballs) {
        count = gumballs;
    }

}
