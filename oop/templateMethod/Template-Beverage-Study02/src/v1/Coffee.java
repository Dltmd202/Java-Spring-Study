package v1;

public class Coffee {
    public void prepareRecipe(){
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }
    private void boilWater(){
        System.out.println("물을 끓임");
    }
    private void brewCoffeeGrinds(){
        System.out.println("커피를 내림");
    }
    private void pourInCup(){
        System.out.println("컵에 따르다");
    }
    private void addSugarAndMilk(){
        System.out.println("밀크와 설탕 추가");
    }
}
