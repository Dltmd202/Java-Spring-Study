public class CeilingFanHighCommand implements Command{
    private CeilingFan fan;
    private CeilingFan.SPEED previousSpeed = CeilingFan.SPEED.OFF;
    public CeilingFanHighCommand(CeilingFan fan){
        this.fan = fan;
    }
    @Override
    public void execute() {
        if(fan.getSpeed() != CeilingFan.SPEED.HIGH){
            previousSpeed = fan.getSpeed();
            fan.setSpeed(CeilingFan.SPEED.HIGH);
        }
    }

    @Override
    public void undo() {
        fan.setSpeed(previousSpeed);
    }
}
