public interface GumballState {
    default void insertCoin() {}
    void ejectCoin();
    void turnCrank();
    void dispense();
    void refill();
}
