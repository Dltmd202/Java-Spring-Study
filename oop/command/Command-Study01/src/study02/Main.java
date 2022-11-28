package study02;

public class Main {
    public static void main(String[] args) {
        RemoteControl remoteControl = new RemoteControl();
        RoomLight roomLight = new RoomLight("안방");
        Stereo stereo = new Stereo();
        CeilingFan ceilingFan = new CeilingFan();

        Command roomLightOnCommand = new RoomLightOnCommand(roomLight);
        Command roomLightOffCommand = new RoomLightOffCommand(roomLight);
        Command stereoOnWithCDCommand = new StereoOnWithCDCommand(stereo);
        Command stereoOnWithUSBCommand = new StereoOnWithUSBCommand(stereo);
        Command stereoOffCommand = new StereoOffCommand(stereo);
        Command ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);
        Command ceilingFanOffCommand = new CeilingFanOffCommand(ceilingFan);

        remoteControl.setCommand(0, roomLightOnCommand, roomLightOffCommand);
        remoteControl.setCommand(1, stereoOnWithCDCommand, stereoOffCommand);
        remoteControl.setCommand(2, stereoOnWithUSBCommand, stereoOffCommand);
        remoteControl.setCommand(3, ceilingFanHighCommand, ceilingFanOffCommand);

        remoteControl.onButtonWasPushed(0);
        remoteControl.undoButtonWasPressed();
        remoteControl.onButtonWasPushed(2);
        remoteControl.onButtonWasPushed(1);
        remoteControl.offButtonWasPushed(1);
        remoteControl.undoButtonWasPressed();
        remoteControl.onButtonWasPushed(3);
        remoteControl.offButtonWasPushed(3);
        remoteControl.undoButtonWasPressed();


    }
}
