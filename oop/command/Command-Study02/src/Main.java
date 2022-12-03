public class Main {
    public static void main(String[] args) {
        RemoteControl remoteControl = new RemoteControl();

        // 처리자를 생성함
        RoomLight rooom = new RoomLight("안방");
        Stereo stereo = new Stereo();
        CeilingFan ceilingFan = new CeilingFan();

        // 구체적인 명령을 생성하고, 처리자와 연결함
        Command roomLightOnCommand = new RoomLightOnCommand(rooom);
        Command roomLightOffCommand = new RoomLightOffCommand(rooom);
        Command stereoOnWithCDCommand = new StereoOnWithCDCommand(stereo);
        Command stereoOnWithUSBCommand = new StereoOnWithUSBCommand(stereo);
        Command stereoOffCommand = new StereoOffCommand(stereo);
        Command ceilingFanOffCommand = new CeilingFanOffCommand(ceilingFan);
        Command ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);

        remoteControl.setCommand(0, roomLightOnCommand, roomLightOffCommand);
        remoteControl.setCommand(1, stereoOnWithCDCommand, stereoOffCommand);
        remoteControl.setCommand(2, stereoOnWithUSBCommand, stereoOffCommand);
        remoteControl.setCommand(3, ceilingFanHighCommand, ceilingFanOffCommand);

        remoteControl.onButtonWasPressed(0);
        remoteControl.undoButtonWasPressed();
        remoteControl.onButtonWasPressed(2);
        remoteControl.onButtonWasPressed(1);
        remoteControl.offButtonWasPressed(1);
        remoteControl.undoButtonWasPressed();
        remoteControl.onButtonWasPressed(3);
        remoteControl.offButtonWasPressed(3);
        remoteControl.undoButtonWasPressed();
    }
}