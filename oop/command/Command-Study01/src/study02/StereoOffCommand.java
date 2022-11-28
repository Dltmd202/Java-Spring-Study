package study02;

public class StereoOffCommand implements Command{
    private Stereo stereo;
    private boolean previousOn = false;
    private Stereo.InputType previousInput = Stereo.InputType.CD;
    private int previousVolume = 0;

    public StereoOffCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    @Override
    public void execute() {
        if(stereo.isOn()){
            previousOn = true;
            previousInput = stereo.getCurrentInput();
            previousVolume = stereo.getVolume();
            stereo.off();
        }
    }

    @Override
    public void undo() {
        if(previousOn){
            stereo.on();
            stereo.setInput(previousInput);
            stereo.setVolume(previousVolume);
        }
    }
}
