package frog.dynamicproxy;

import frog.Frog;
import frog.NoBatteryToBehaveException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 데코레이터 패턴을 적용한 RobotFrogProxy
 * 대리자를 이용하여 부가기능을 적용시켜줄 수 있는 데코레이터 패턴을 적용하였다.
 * 먼저 해당 클래스는 실제 원래의 jump(), croak() 메서드를 가지고 있는 target 객체를 가지고 있다.
 * 이때 공통적으로 모든 메서드에 보편적으로 적용할 수 있도록 Class 객체의 메서드를 추출하여 배터리가 적용된 상태로
 * invoke 될 수 있도록 구성하였다. 
 * 또 Frog 자체를 상속하여 jump(), crock() 메서드를 오버라이드하여 다형성을 통해 Frog로 해당 클래스의 인스턴스를
 * 받을 수 있도록 하였다.
 * <br/>
 * 해당 Frog가 interface로 되어있다면 JDK Dynamic Proxy 모듈을 이용하여 좀 더 일반화된 확장 방법을 제공할 수 있다.
 */
public class RobotFrogProxy extends Frog implements InvocationHandler {
    
    private Object target;
    private int battery;
    private Class frogClass;
    
    public RobotFrogProxy() {
        this.battery = 5;
        this.target = new Frog();
        try {
            frogClass = Class.forName("frog.Frog");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void jump() {
        try {
            Method jumpMethod = frogClass.getMethod("jump");
            invoke(target, jumpMethod, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (NoBatteryToBehaveException e){
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void croak() {
        try {
            Method croakMethod = frogClass.getMethod("croak");
            invoke(target, croakMethod, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (NoBatteryToBehaveException e){
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!isValidBehavior()) {
            String methodName = method.getName();
            throw new NoBatteryToBehaveException(
                    String.format("배터리가 부족하여 %s()을 할 수 업습니다.", methodName)
            );
        }
        battery--;
        return method.invoke(target, args);
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