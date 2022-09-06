package frog.composition;

import frog.Frog;
import frog.NoBatteryToBehaveException;

/**
 * 포함 관계의 로봇게구리 
 * <br/>
 * 포함관계를 사용하여 RobotFrog로 확장했을 경우 장점은
 * 상속을 하고 있지 않기 때문에 다른 클래스를 상속하기 용의하다.
 * <br/>
 * 단점은 만약 매개변수의 인자로 해당 객체 인스턴스를 주입을 할 수 없다. 
 * 다형성을 통한 메서드 호출이 불가능하다.
 */
public class RobotFrogComposition {
    
    private int battery;
    private Frog frog;

    public RobotFrogComposition() {
        this.battery = 5;
        frog = new Frog();
    }

    public RobotFrogComposition(Frog frog) {
        this.frog = frog;
    }

    public void jump(){
        if(!isValidBehavior()) 
            throw new NoBatteryToBehaveException("배터리가 부족하여 jump()를 할 수 업습니다.");
        battery--;
        frog.jump();
    }
    
    public void croak(){
        if(!isValidBehavior())
            throw new NoBatteryToBehaveException("배터리가 부족하여 croak()을 할 수 업습니다.");
        battery--;
        frog.croak();
    }

    public void charge(){
        try {
            while (this.battery < 5){
                Thread.sleep(100);
                System.out.println("충전중... 현재 베터리: " + battery);
                this.battery++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("충전 완료! 현재 베터리: " + battery);
    }

    private boolean isValidBehavior(){
        return battery > 0;
    }
}