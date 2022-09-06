package frog.inherit;

import frog.Frog;
import frog.NoBatteryToBehaveException;

/**
 * 상속을 이용한 로봇게구리 
 * <br/>
 * 상속을 사용하기 때문에 RobotFrog로 확장했을 때 장점은
 * 개구리가 가지는 공통 jump와 crock이 배터리의 제약을 받도록 재정의 하였다.
 * 상속을 이용하면 함수의 인자로 RobotFrogInherited 인스턴스를 주더라도
 * Frog형으로 받았을 때 crock 메서드를 실행하면 배터리가 적용된 메서드가 호출된다.
 * <br/>
 * 단점은 만약 Frog에 필요없거나, 상속된 해당 클래스에 적합하지 않은 메서드가 있다면
 * 빈메서드로 재정의 해야한다.
 */
public class RobotFrogInherited extends Frog {

    private int battery;

    public RobotFrogInherited() {
        this.battery = 5;
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

    @Override
    public void jump() {
        if(!isValidBehavior()) 
            throw new NoBatteryToBehaveException("배터리가 부족하여 jump()를 할 수 업습니다.");
        battery--;
        super.jump();
    }

    @Override
    public void croak() {
        if(!isValidBehavior())
            throw new NoBatteryToBehaveException("배터리가 부족하여 croak()을 할 수 업습니다.");
        battery--;
        super.croak();
    }

    private boolean isValidBehavior(){
        return battery > 0;
    }
}