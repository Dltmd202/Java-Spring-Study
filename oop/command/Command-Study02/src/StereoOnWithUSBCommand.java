public class StereoOnWithUSBCommand implements Command{
    private Stereo stereo;
    private boolean wasOn;
    private int previousVolume;
    private Stereo.InputType previousInputType = Stereo.InputType.CD;

    public StereoOnWithUSBCommand(Stereo stereo){
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        wasOn = stereo.isOn();
        previousInputType = stereo.getCurrentInput();
        previousVolume = stereo.getVolume();
        stereo.on();
        stereo.setInput(Stereo.InputType.USB);
        stereo.setVolume(11);
    }

    @Override
    public void undo() {
        if(wasOn){
            stereo.on();
            stereo.setInput(previousInputType);
            stereo.setVolume(previousVolume);
        } else stereo.on();
    }
}
