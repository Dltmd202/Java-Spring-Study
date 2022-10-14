public abstract class Pizza {
    private String name;

    protected Pizza(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void prepare(){
        System.out.printf("준비중: %s%n", name);
    }
    public void bake(){
        System.out.println("25분 동안 350도에서 굽다.");
    }
    public void cut(){
        System.out.println("피자를 8조각으로 짜른다.");
    }
    public void box(){
        System.out.println("포장합니다!");
    }

}
