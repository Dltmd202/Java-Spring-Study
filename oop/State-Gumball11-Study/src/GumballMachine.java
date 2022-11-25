/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 상태 패턴
 * GumballMachine.java
 * 문맥 클래스
 * Context driven transition (문맥 중심 전이)
 * 문맥 중심에서도 문맥 객체를 전이 메소드의 인자로 전달하여
 * 각 상태에서 문맥이 해야 하는 일을 상태에 위임할 수 있음
 */
public class GumballMachine {
    // 상태 중심과 비교
    // private final GumballState soldOutState = new SoldOutState(this);
    private static final GumballState soldOutState = new SoldOutState();
    private static final GumballState soldState = new SoldState();
    private static final GumballState noCoinState = new NoCoinState();
    private static final GumballState hasCoinState = new HasCoinState();
    private GumballState currentState;
    private int gumballs = 0;

    public GumballMachine(int numberGumballs) {
        gumballs = numberGumballs;
        if(gumballs > 0) currentState = noCoinState;
        else currentState = soldOutState;
    }
    public void insertCoin(){
        if(currentState.insertCoin(this)) currentState = hasCoinState;
    }
    public void ejectCoin(){
        if(currentState.ejectCoin(this)) currentState = noCoinState;
    }
    public void turnCrank(){
        if(currentState.turnCrank(this)) {
            currentState = soldState;
            if(currentState.dispense(this)){
                if(gumballs==0){
                    System.out.println("껌볼이 더 이상 없습니다.");
                    currentState = soldOutState;
                }
                else currentState = noCoinState;
            }
        }
    }
    public void refill() {
        if(currentState.refill(this))
            currentState = noCoinState;
    }
    public int getNumberOfGumballs(){
        return gumballs;
    }
    void dispense(){
        if(gumballs>0) --gumballs;
        System.out.println("현재 컴볼 수: "+gumballs);
    }
    void refill(int gumballs) {
        this.gumballs = gumballs;
    }
}
