public class Main {
}

/**
 * 잘못된 설계
 */
interface MisunderstoodLivingThing{
    void eat();

    void walk();

    void swim();

    void fly();
}

interface LivingThing{
    void eat();
}

interface LivingInSky extends LivingThing{
    void fly();
}

interface LivingOnLand extends LivingThing{
    void walk();
}

interface LivingInWater extends LivingThing{
    void swim();
}

class Frog implements LivingInWater, LivingOnLand{

    @Override
    public void eat() {

    }

    @Override
    public void walk() {

    }

    @Override
    public void swim() {

    }
}

class Duck implements LivingInSky, LivingInWater, LivingOnLand{

    @Override
    public void eat() {

    }

    @Override
    public void fly() {

    }

    @Override
    public void walk() {

    }

    @Override
    public void swim() {

    }
}