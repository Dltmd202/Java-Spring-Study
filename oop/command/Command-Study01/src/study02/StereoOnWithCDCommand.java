package study02;

public class StereoOnWithCDCommand implements Command{
    private Stereo stereo;
    private boolean isOn = false;
    private Stereo.InputType inputType = Stereo.InputType.CD;
    private int volume = 0;

    public StereoOnWithCDCommand(Stereo stereo){
        this.stereo = stereo;
    }
    @Override
    public void execute() {
        isOn = stereo.isOn();
        inputType = stereo.getCurrentInput();
        volume = stereo.getVolume();
        stereo.on();
        stereo.setInput(Stereo.InputType.CD);
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
