public interface DoorState {
    boolean open();
    boolean close();
    boolean lock();
    boolean unlock();
}