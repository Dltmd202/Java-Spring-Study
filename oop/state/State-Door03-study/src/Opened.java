public class Opened implements DoorState{
    @Override
    public boolean open() {
        System.out.println("이미 열려 있음");
        return false;
    }

    @Override
    public boolean close() {
        System.out.println("문이 열림");
        return true;
    }

    @Override
    public boolean lock() {
        System.out.println("문이 열려 있어 잠글 수 없음");
        return false;
    }

    @Override
    public boolean unlock() {
        System.out.println("문이 잠겨 있지 않음");
        return false;
    }
}
