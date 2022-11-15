public class RoomLightOnCommand implements Command{
    private RoomLight light;

    public RoomLightOnCommand(RoomLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}
