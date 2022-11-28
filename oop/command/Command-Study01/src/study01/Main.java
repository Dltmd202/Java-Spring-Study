package study01;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
        test01();
    }

    public static void test01(){
        SimpleRemoteControl remote = new SimpleRemoteControl();
        RoomLight roomLight = new RoomLight("거실");
        LightOnCommand lightOn = new LightOnCommand(roomLight);

        remote.setCommand(lightOn);
        remote.buttonWasPressed();
    }
}