package study02;

public class StereoOnWithUSBCommand implements Command{
    private Stereo stereo;
    private boolean isOn = false;
    private Stereo.InputType inputType = Stereo.InputType.USB;
    private int volume = 0;

    public StereoOnWithUSBCommand(Stereo stereo){
        this.stereo = stereo;
    }
    @Override
    public void execute() {
        isOn = stereo.isOn();
        inputType = stereo.getCurrentInput();
        volume = stereo.getVolume();
        stereo.on();
        stereo.setInput(Stereo.InputType.USB);
        stereo.setVolume(11);
    }

    @Override
    public void undo() {
        if(isOn){
            stereo.on();
            stereo.setInput(inputType);
            stereo.setVolume(volume);
        } else stereo.off();
    }
}
