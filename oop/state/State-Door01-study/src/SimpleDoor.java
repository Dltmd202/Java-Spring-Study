public class SimpleDoor {
    private DoorState currentState = DoorState.CLOSED;

    public DoorState getState(){
        return currentState;
    }

    public void open(){
        if(currentState == DoorState.CLOSED)
            currentState = DoorState.OPENED;
    }

    public void close(){
        if(currentState == DoorState.OPENED)
            currentState = DoorState.CLOSED;
    }

    public void lock(){
        if(currentState == DoorState.CLOSED)
            currentState = DoorState.LOCKED;
    }

}
