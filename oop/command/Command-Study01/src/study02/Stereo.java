package study02;

public class Stereo {
    public enum InputType {CD, RADIO, USB};
    private boolean isOn = false;
    private InputType currentInput = InputType.CD;
    private int volume = 0;

    public void on(){
        isOn = true;
        System.out.println("스테레오 전원 켜짐");
    }

    public void off(){
        isOn = false;
        System.out.println("스테레오 전원 꺼짐");
    }

    public boolean isOn(){
        return isOn;
    }

    public InputType getCurrentInput(){
        return currentInput;
    }

    public int getVolume(){
        return volume;
    }

    public void setInput(InputType inputType){
        if(currentInput != inputType){
            currentInput = inputType;
            switch (currentInput){
                case CD:
                    setCD();
                case RADIO:
                    setRadio();
                case USB:
                    setUSB();
            }
        }
    }

    private void setUSB() {
        System.out.println("스테레오 입력 CD로 바꿈");
    }

    private void setRadio() {
        System.out.println("스테레오 입력 라디오로 바꿈");
    }

    private void setCD() {
        System.out.println("스테레오 입력 CD로 바꿈");
    }

    public void setVolume(int volume){
        this.volume = volume;
        System.out.println("볼륨 조정 " + volume);
    }
}
