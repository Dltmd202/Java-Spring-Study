public class GumballMachine {
    private GumballState currentState;

    private int count = 0;

    public GumballMachine(int numberGumballs){
        count = numberGumballs;
        if(count > 0) currentState = GumballState.NO_COIN;
        else currentState = GumballState.SOLD_OUT;
    }

    public void insertCoin(){
        switch (currentState){
            case SOLD_OUT:
                System.out.println("껌볼이 없어 판매가 중단됨");
                break;
            case NO_COIN:
                System.out.println("동전이 삽입되었음");
                currentState = GumballState.HAS_COIN;
                break;
            case SOLD:
                System.out.println("동전을 투입할 수 있는 단계가 아님");
            case HAS_COIN:
                System.out.println("이미 동전이 있음");
                break;
        }
    }

    public void ejectCoin(){
        switch (currentState){
            case SOLD_OUT:
                System.out.println("껌볼이 없어 판매가 중단됨");
                break;
            case NO_COIN:
                System.out.println("반환할 동전이 없음");
                break;
            case SOLD:
                System.out.println("반환할 동전이 없음");
                break;
            case HAS_COIN:
                System.out.println("삽입된 동전을 반환함");
                currentState = GumballState.NO_COIN;
        }
    }

    public void turnCrank(){
        switch (currentState){
            case SOLD_OUT:
                System.out.println("껌볼이 없어 판매가 중단됨");
                break;
            case NO_COIN:
                System.out.println("동전이 없어 손잡이를 돌릴 수 없음");
                break;
            case SOLD:
                System.out.println("이미 손잡이를 돌렸음");
                break;
            case HAS_COIN:
                System.out.println("손잡이를 돌렸음");
                currentState =  GumballState.SOLD;
                dispense();
                break;
        }
    }

    public void dispense(){
        switch (currentState){
            case SOLD_OUT:
                System.out.println("껌볼이 없어 판매가 중단됨");
                break;
            case NO_COIN:
                System.out.println("동전을 투입해야 구입할 수 있음");
                break;
            case SOLD:
                --count;
                System.out.format("껌볼이 나옴. 남은 껌볼 수: %d\n", count);
                if(isEmpty()){
                    System.out.println("껌볼이 더 이상 없습니다.");
                    currentState = GumballState.SOLD_OUT;
                } else currentState = GumballState.NO_COIN;
            case HAS_COIN:
                System.out.println("손잡이를 돌려야 껌볼이 나옴");
                break;
        }
    }

    public void refill(){
        switch (currentState){
            case SOLD_OUT:
                System.out.println("껌볼을 추가함");
                count = 20;
                currentState = GumballState.NO_COIN;
                break;
            case NO_COIN, SOLD, HAS_COIN:
                System.out.println("껌볼의 추가는 껌볼이 없을 때만 가능");
                break;
        }
    }

    public int getNumberOfGumballs(){
        return count;
    }

    private boolean isEmpty() {
        return count == 0;
    }
}
