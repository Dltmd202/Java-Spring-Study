public class Closed implements DoorState{
    private Door door;

    public Closed(Door door){
        this.door = door;
    }

    @Override
    public void open() {
        System.out.println("문이 열림");
    }

    @Override
    public void close() {
        System.out.println("문이 닫힘");
        door.changeToClosedState();
    }

    @Override
    public void lock() {
        System.out.println("문이 열려 있어 잠글 수 없음");
    }

    @Override
    public void unlock() {
        System.out.println("문이 잠겨 있지 않음");
    }
}
