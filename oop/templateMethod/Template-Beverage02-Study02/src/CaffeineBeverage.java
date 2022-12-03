public abstract class CaffeineBeverage {
    public abstract void prepareRecipe();

    protected void boilWater(){
        System.out.println("물을 끓임");
    }

    protected void pourInCup(){
        System.out.println("컵에 따르다");
    }
}
