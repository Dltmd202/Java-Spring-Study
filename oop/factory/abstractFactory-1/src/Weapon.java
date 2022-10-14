import java.util.concurrent.ThreadLocalRandom;

public abstract class Weapon {
    private int bound;
    public Weapon(int bound) {
        this.bound = bound;
    }
    public int damage(){
        return ThreadLocalRandom.current().nextInt(bound) + 1;
    }

    public abstract int weight();
}

class Lance extends Weapon{

    public Lance() {
        super(12);
    }

    @Override
    public int weight() {
        return 6;
    }
}

class Dagger extends Weapon{

    public Dagger() {
        super(4);
    }

    @Override
    public int weight() {
        return 1;
    }
}

class Longbow extends Weapon{

    public Longbow() {
        super(8);
    }

    @Override
    public int weight() {
        return 2;
    }
}

class HandAxe extends Weapon{

    public HandAxe() {
        super(6);
    }

    @Override
    public int weight() {
        return 2;
    }
}