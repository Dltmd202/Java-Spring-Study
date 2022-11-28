public class Main {
    public static void simulate(Target target){
        target.foo();
    }

    public static void main(String[] args) {
        Dog dog = new Dog();
        Cat cat = new Cat();
        simulate(new Adapter(dog, "bark"));
        simulate(new Adapter(cat, "meow"));
    }
}