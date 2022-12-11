package v3;

public class Coffee extends CaffeineBeverage{
    @Override
    protected void brew(){
        System.out.println("커피를 내림");
    }

    @Override
    protected void addCondiment(){
        System.out.println("밀크와 설탕 추가");
    }
}
