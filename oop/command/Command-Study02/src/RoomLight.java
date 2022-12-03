public class RoomLight {
    private String location;

    public RoomLight(String location){
        this.location = location;
    }

    public void on(){
        System.out.println(location + ": 불이 켜짐");
    }

    public void off(){
        System.out.println(location + ": 불이 꺼짐");
    }
}
