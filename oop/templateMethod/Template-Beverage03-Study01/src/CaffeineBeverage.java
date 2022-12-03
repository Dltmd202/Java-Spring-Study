public abstract class CaffeineBeverage {
    public final void prepareRecipe(){
        boilWater();
        brew();
        pourInCup();
        if(customerWantsCondiment())
            addCondiment();
    }

    private void boilWater(){
        System.out.println("물을 끓임");
    }

    protected abstract void brew();

    private void pourInCup(){
        System.out.println("컵에 따르다");
    }

    protected abstract void addCondiment();

    protected boolean customerWantsCondiment(){
        return true;
    }
}
