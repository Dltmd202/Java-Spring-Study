public class SteroOffCommand implements Command{
    private Stero stero;
    private boolean previouslyOn = false;
    private Stero.InputType previousInput = Stero.InputType.CD;
    private int previousVolume = 0;

    public SteroOffCommand(Stero stero) {
        this.stero = stero;
    }

    @Override
    public void execute() {
        if(stero.isOn()){
            previouslyOn = true;
            previousInput = stero.getCurrentInput();
            previousVolume = stero.getVolume();
            stero.off();
        } else {
            previouslyOn = false;
        }
    }

    @Override
    public void undo() {
        if(previouslyOn){
            stero.on();
            stero.setInput(previousInput);
            stero.setVolume(previousVolume);
        }
    }
}
