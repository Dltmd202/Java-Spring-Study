public interface GumballState {
    boolean insertCoin();
    boolean ejectCoin();
    boolean turnCrank();
    boolean dispense();
    boolean refill();
}
