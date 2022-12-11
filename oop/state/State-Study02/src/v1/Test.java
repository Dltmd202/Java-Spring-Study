package v1;
public class Test {
	public static void main(String[] args) {
		GumballMachine myMachine = new GumballMachine(5);
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.insertCoin();
		myMachine.turnCrank();
		myMachine.refill();
		myMachine.insertCoin();
		myMachine.turnCrank();
	}
}
