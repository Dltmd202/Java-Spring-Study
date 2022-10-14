public class Pizza {
    private String name;

    public Pizza(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name;
    }

    public void prepare(){
        System.out.println("preparing: " + name);
    }


    public void bake(){
        System.out.println("baking... ");
    }

    public void cut(){
        System.out.println("cutting... ");
    }

    public void box(){
        System.out.println("boxing... ");
    }

}
