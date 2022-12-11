package v2;

public class Coffee extends CaffeineBeverage{
    @Override
    public void prepareRecipe() {
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }

    private void brewCoffeeGrinds(){
        System.out.println("커피를 내림");
    }
    private void addSugarAndMilk(){
        System.out.println("밀크와 설탕 추가");
    }
}
