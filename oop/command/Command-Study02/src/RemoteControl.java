public class RemoteControl {
    private Command[] onCommands = new Command[5];
    private Command[] offCommands = new Command[5];
    private Command undoCommand = NoCommand.unique;


    public RemoteControl() {
        for (int i = 0; i < 5; i++) {
            onCommands[i] = NoCommand.unique;
            offCommands[i] = NoCommand.unique;
        }
    }

    public void setCommand(int slot, Command onCommand, Command offCommand){
        if(slot < 0 || slot >= onCommands.length)
            throw new ArrayIndexOutOfBoundsException("없는 slot");
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonWasPressed(int slot){
        if(slot < 0 || slot >= onCommands.length)
            throw new ArrayIndexOutOfBoundsException("없는 slot");
        onCommands[slot].execute();
        undoCommand = onCommands[slot];
    }

    public void offButtonWasPressed(int slot){
        if(slot < 0 || slot >= offCommands.length)
            throw new ArrayIndexOutOfBoundsException("없는 slot");
        offCommands[slot].execute();
        undoCommand = offCommands[slot];
    }

    public void undoButtonWasPressed(){
        undoCommand.execute();
        undoCommand = NoCommand.unique;
    }
}
