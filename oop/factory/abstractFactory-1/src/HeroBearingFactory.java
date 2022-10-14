public abstract class HeroBearingFactory {
    abstract Weapon createWeapon();

    abstract Armor createArmor();
}

class ArcherBearingFactory extends HeroBearingFactory{

    @Override
    Weapon createWeapon() {
        return new Longbow();
    }

    @Override
    Armor createArmor() {
        return new LeatherArmor();
    }
}

class PaladinBearingFactory extends HeroBearingFactory{
    @Override
    Weapon createWeapon() {
        return new Lance();
    }
    @Override
    Armor createArmor() {
        return new ChainMail();
    }
}

class ThiefBearingFactory extends HeroBearingFactory{
    @Override
    Weapon createWeapon() {
        return new Dagger();
    }
    @Override
    Armor createArmor() {
        return new LeatherArmor();
    }
}