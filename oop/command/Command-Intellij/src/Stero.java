public class Stero {
    public enum InputType {CD, RADIO, USB};
    public boolean isOn = false;
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
                case CD :
                    setCD();
                    break;
                case RADIO:
                    setRadio();
                    break;
                default:
                    setUSB();
                    break;
            }
        }
    }

    private void setCD(){
        System.out.println("스테레오 입력 CD로 바꿈");
    }

    private void setRadio(){
        System.out.println("스테레오 입력 라디오로 바꿈");
    }

    private void setUSB(){
        System.out.println("스테레오 입력 USB로 바꿈");
    }

    public void setVolume(int volume){
        this.volume = volume;
        System.out.format("볼륨 조정 %d\n", volume);
    }
}
