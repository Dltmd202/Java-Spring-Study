package v3;

public abstract class CaffeineBeverage {
    public final void prepareRecipe(){
        boilWater();
        brew();
        pourInCup();
        if(customerWantsCondiments())
            addCondiment();
    }

    private void boilWater(){
        System.out.println("물을 끓임");
    }

    protected abstract void brew();

    protected abstract void addCondiment();

    protected boolean customerWantsCondiments(){
        return false;
    }

    private void pourInCup(){
        System.out.println("컵에 따르다");
    }
}
