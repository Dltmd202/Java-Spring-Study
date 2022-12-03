public class StereoOffCommand implements Command{
    private Stereo stereo;
    private boolean previouslyOn = false;
    private Stereo.InputType previousInput = Stereo.InputType.CD;
    private int previousVolume = 0;

    public StereoOffCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        if(stereo.isOn()){
            previouslyOn = true;
            previousInput = stereo.getCurrentInput();
            previousVolume = stereo.getVolume();
            stereo.off();
        } else previouslyOn = false;
    }

    @Override
    public void undo() {
        if(previouslyOn){
            stereo.on();
            stereo.setInput(previousInput);
            stereo.setVolume(previousVolume);
        }
    }
}
