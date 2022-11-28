package study02;

public class RoomLightOffCommand implements Command{
    private RoomLight light;

    public RoomLightOffCommand(RoomLight light){
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}
