public class Door {
    private static final DoorState openedState = new Opened();
    private static final DoorState closedState = new Closed();
    private static final DoorState lockedState = new Locked();
    private DoorState currentState = closedState;

    public DoorState getState(){
        return currentState;
    }

    public void open(){
        if(currentState.open()) currentState = openedState;
    }

    public void close(){
        if(currentState.close()) currentState = closedState;
    }

    public void lock(){
        if(currentState.lock()) currentState = lockedState;
    }

    public void unlock(){
        if(currentState.unlock()) currentState = closedState;
    }
}
