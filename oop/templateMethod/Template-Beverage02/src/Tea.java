public class Tea extends CaffeineBeverage{

    @Override
    public void prepareRecipe() {

    }

    private void steepTeaBag(){
        System.out.println("티백을 담그다");
    }

    private void addLemon(){
        System.out.println("레몬 추가");
    }
}
