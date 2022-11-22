public class Opened implements DoorState{

    private Door door;

    public Opened(Door door){
        this.door = door;
    }

    @Override
    public void open() {
        System.out.println("이미 열려 있음");
    }

    @Override
    public void close() {
        System.out.println("문이 닫힘");

    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }
}
