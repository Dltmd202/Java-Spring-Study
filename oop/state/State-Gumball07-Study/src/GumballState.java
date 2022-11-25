public interface GumballState {
    void insertCoin(GumballMachine gumballMachine);
    void ejectCoin(GumballMachine gumballMachine);
    void turnCrank(GumballMachine gumballMachine);
    void dispense(GumballMachine gumballMachine);
    void refill(GumballMachine gumballMachine);
}
