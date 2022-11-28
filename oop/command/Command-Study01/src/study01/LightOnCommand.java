package study01;

public class LightOnCommand implements Command {
    private RoomLight roomLight;

    public LightOnCommand(RoomLight roomLight) {
        this.roomLight = roomLight;
    }

    @Override
    public void execute() {
        roomLight.on();
    }
}
