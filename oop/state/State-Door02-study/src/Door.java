public class Door {
    private final DoorState openedState = new Opened(this);

    private final DoorState closedState = new Closed(this);

    private final DoorState lockedState = new Locked(this);

    private DoorState currentState = closedState;

    void changeToOpenedState(){
        currentState = openedState;
    }

    void changeToClosedState(){
        currentState = closedState;
    }

    void changeToLockedState(){
        currentState = lockedState;
    }

    public void open(){
        currentState.open();
    }

    public void close(){
        currentState.close();
    }

    public void lock(){
        currentState.lock();
    }

    public void unlock(){
        currentState.unlock();
    }
}
