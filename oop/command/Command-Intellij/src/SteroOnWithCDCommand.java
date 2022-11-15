public class SteroOnWithCDCommand implements Command{
    private Stero stero;
    private boolean isOn = false;
    private Stero.InputType inputType = Stero.InputType.CD;
    private int volume = 0;

    public SteroOnWithCDCommand(Stero stero) {
        this.stero = stero;
    }

    @Override
    public void execute() {
        isOn = stero.isOn();
        inputType = stero.getCurrentInput();
        volume = stero.getVolume();
        stero.on();
        stero.setInput(Stero.InputType.CD);
        stero.setVolume(11);
    }

    @Override
    public void undo() {
        if(isOn){
            stero.on();
            stero.setInput(inputType);
            stero.setVolume(volume);
        } else
            stero.off();
    }
}
