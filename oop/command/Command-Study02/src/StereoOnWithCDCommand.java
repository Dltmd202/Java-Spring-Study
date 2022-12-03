public class StereoOnWithCDCommand implements Command{
    private Stereo stereo;
    private boolean wasOn = false;
    private Stereo.InputType previousInputType = Stereo.InputType.CD;
    private int previousVolume = 0;

    public StereoOnWithCDCommand(Stereo stereo){
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        wasOn = stereo.isOn();
        previousInputType = stereo.getCurrentInput();
        previousVolume = stereo.getVolume();
        stereo.on();
        stereo.setInput(Stereo.InputType.CD);
        stereo.setVolume(11);
    }

    @Override
    public void undo() {
        if(wasOn){
            stereo.on();
            stereo.setInput(previousInputType);
            stereo.setVolume(previousVolume);
        } else stereo.off();
    }
}
