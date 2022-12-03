public class Tea extends CaffeineBeverage{
    @Override
    protected void brew() {
        System.out.println("티백을 담그다");
    }

    @Override
    protected void addCondiment() {
        System.out.println("레몬 추가");
    }
}
