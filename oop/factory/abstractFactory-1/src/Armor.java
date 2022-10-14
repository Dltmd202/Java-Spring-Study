public abstract class Armor {
    public abstract int armorClass(int modifier);
    public abstract int weight();
}

class LeatherArmor extends Armor{

    @Override
    public int armorClass(int modifier) {
        return 11 + modifier;
    }

    @Override
    public int weight() {
        return 10;
    }
}

// ChainMail 갑옷
class ChainMail extends Armor{
    @Override public int armorClass(int modifier) {
        return 16;
    }
    @Override public int weight() {
        return 55;
    }
}