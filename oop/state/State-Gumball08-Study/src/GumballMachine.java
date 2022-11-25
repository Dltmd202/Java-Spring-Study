public class GumballMachine {
    private GumballState currentState;
    private int count = 0;
    public GumballMachine(int numberGumballs) {
        count = numberGumballs;
        if(count > 0) currentState = GumballState.NOCOINSTATE;
        else currentState = GumballState.SOLDOUTSTATE;
    }
    public void insertCoin(){
        if(currentState.insertCoin()) currentState = GumballState.HASCOINSTATE;
    }
    public void ejectCoin(){
        if(currentState.ejectCoin()) currentState = GumballState.NOCOINSTATE;
    }
    public void turnCrank(){
        if(currentState.turnCrank()) currentState = GumballState.SOLDSTATE;
        if(currentState.dispense()){
            dispense();
            if(count==0){
                System.out.println("껌볼이 더 이상 없습니다.");
                currentState = GumballState.SOLDOUTSTATE;
            }
            else{
                currentState = GumballState.NOCOINSTATE;
            }
        }
    }
    public void refill() {
        if(currentState.refill()) {
            count = 20;
            currentState = GumballState.NOCOINSTATE;
        }
    }
    public int getNumberOfGumballs(){
        return count;
    }
    public void dispense(){
        if(count>0) --count;
        //	System.out.println(count);
    }
}
