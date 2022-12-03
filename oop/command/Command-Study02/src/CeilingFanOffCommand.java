public class CeilingFanOffCommand implements Command{
    private CeilingFan fan;
    private CeilingFan.SPEED previousSpeed = CeilingFan.SPEED.OFF;

    public CeilingFanOffCommand(CeilingFan fan){
        this.fan = fan;
    }
    @Override
    public void execute() {
        if(fan.getSpeed() != CeilingFan.SPEED.OFF){
            previousSpeed = fan.getSpeed();
            fan.setSpeed(CeilingFan.SPEED.OFF);
        }
    }

    @Override
    public void undo() {
        fan.setSpeed(previousSpeed);
    }
}
