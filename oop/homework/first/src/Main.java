import frog.Frog;
import frog.NoBatteryToBehaveException;
import frog.composition.RobotFrogComposition;
import frog.dynamicproxy.RobotFrogProxy;
import frog.inherit.RobotFrogInherited;

/**
 * 2018136088 이승환 
 */
public class Main {
    public static void main(String[] args) {
        inheritedRobotFrogTest();
        compositionRobotFrogTest();
        proxyRobotFrogTest();
    }

    private static void inheritedRobotFrogTest() {
        System.out.println("=========상속=========");
        RobotFrogInherited inheritedFrog = new RobotFrogInherited();
        
        for (int i = 0; i < 10; i++) {
            try {
                if(i % 2 == 0) inheritedFrog.jump();
                else inheritedFrog.croak();
            } catch (NoBatteryToBehaveException e){
                inheritedFrog.charge();
            }
        }
        System.out.println("=======================\n");
    }
    
    private static void compositionRobotFrogTest() {
        System.out.println("=========포함=========");
        RobotFrogComposition robotFrogComposition = new RobotFrogComposition();

        
        for (int i = 0; i < 10; i++) {
            try{
                if(i % 2 == 0) robotFrogComposition.jump();
                else robotFrogComposition.croak();
            } catch (NoBatteryToBehaveException e){
                robotFrogComposition.charge();
            }
        }
        System.out.println("=======================\n");
    }
    
    private static void proxyRobotFrogTest(){
        System.out.println("=======데코레이터 패턴======");
        Frog frog = new Frog();
        RobotFrogProxy robotFrogProxy = new RobotFrogProxy();

        for (int i = 0; i < 10; i++) {
            try{
                if(i % 2 == 0) robotFrogProxy.jump();
                else robotFrogProxy.croak();
            } catch (NoBatteryToBehaveException e){
                robotFrogProxy.charge();
            }
        }
        System.out.println("=======================\n");
    }

}