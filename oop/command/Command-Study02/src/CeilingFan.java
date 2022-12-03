public class CeilingFan {
    public enum SPEED {OFF, LOW, MEDIUM, HIGH};
    private SPEED speed = SPEED.OFF;

    public void setSpeed(SPEED speed){
        if(this.speed != speed){
            this.speed = speed;
            if(speed == SPEED.OFF) System.out.println("선풍기 꺼짐");
            else System.out.printf("선풍기 켜짐. 현재 속도: %d%n", speed.ordinal());
        }
    }

    public SPEED getSpeed(){
        return speed;
    }
}
