public class Door {
    private DoorState currentState = DoorState.CLOSED;

    public DoorState getState() {
        return currentState;
    }

    public void open(){
        switch (currentState){
            case OPENED:
                System.out.println("이미 열려 있음");
                break;
            case CLOSED:
                System.out.println("문이 열림");
                currentState = DoorState.OPENED;
            case LOCKED:
                System.out.println("문이 잠겨 있어 열 수 없음");
                break;
        }
    }

    public void close(){
        switch (currentState){
            case OPENED:
                System.out.println("문이 열려 있어 잠글 수 없음");
                break;
            case CLOSED:
                System.out.println("문을 잠금");
                currentState = DoorState.LOCKED;
                break;
            case LOCKED:
                System.out.println("문이 이미 잠겨 있음");
                break;
        }
    }

    public void unlock(){
        switch (currentState){
            case OPENED, CLOSED:
                System.out.println("문이 잠겨 있지 않음");
                break;
            case LOCKED:
                System.out.println("문의 잠금을 해제함");
                break;
        }
    }
}
