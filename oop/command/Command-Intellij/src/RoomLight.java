public class RoomLight {
    private String location;

    public RoomLight(String location){
        this.location = location;
    }

    public void on(){
        System.out.format("%s: 불이 켜짐\n", location);
    }

    public void off(){
        System.out.format("%s: 불이 꺼짐\n", location);
    }
}
