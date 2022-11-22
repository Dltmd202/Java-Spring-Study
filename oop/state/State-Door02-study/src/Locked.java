public class Locked implements DoorState {

    private Door door;

    @Override
    public void open() {
        System.out.println("문이 잠겨 있어 열 수 없음");
    }

    public Locked(Door door) {
        this.door = door;
    }

    @Override
    public void close() {
        System.out.println("문이 이미 닫혀 있음");
    }

    @Override
    public void lock() {
        System.out.println("문이 이미 잠겨 있음");
    }

    @Override
    public void unlock() {
        System.out.println("문의 잠금을 해제함");
        door.changeToClosedState();
    }
}
