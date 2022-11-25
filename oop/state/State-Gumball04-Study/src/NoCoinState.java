public class NoCoinState implements GumballState{
    @Override
    public boolean insertCoin() {
        System.out.println("동전이 삽입되었음");
        return true;
    }

    @Override
    public boolean ejectCoin() {
        System.out.println("반환할 동전 없음");
        return false;
    }

    @Override
    public boolean turnCrank() {
        System.out.println("동전이 없어 손잡이를 돌릴 수 없음");
        return false;
    }

    @Override
    public boolean dispense() {
        System.out.println("동전을 투입해야 구입할 수 있음");
        return false;
    }

    @Override
    public boolean refill() {
        System.out.println("껌볼이 없는 경우에는 껌볼을 채울 수 있음");
        return false;
    }
}
