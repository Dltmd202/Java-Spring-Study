public abstract class Beverage {
    private String description = "이름없는 음료";

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public abstract int cost();

    public Beverage removeCondiment(){
        return this;
    }
}
