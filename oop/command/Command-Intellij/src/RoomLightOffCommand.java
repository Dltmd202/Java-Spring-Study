public class RoomLightOffCommand implements Command{
    private RoomLight light;
    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}
