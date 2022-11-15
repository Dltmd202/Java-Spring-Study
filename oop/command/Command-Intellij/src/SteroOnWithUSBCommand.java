public class SteroOnWithUSBCommand implements Command{
    private Stero stero;
    private boolean isOn = false;
    private Stero.InputType inputType = Stero.InputType.CD;
    private int volume = 0;

    @Override
    public void execute() {
        isOn = stero.isOn();
        inputType = stero.getCurrentInput();
        volume = stero.getVolume();
        stero.on();
        stero.setInput(Stero.InputType.USB);
        stero.setVolume(11);
    }
    @Override
    public void undo() {
        if(isOn){
            stero.on();
            stero.setInput(inputType);
            stero.setVolume(volume);
        }
        else stero.off();
    }
}
