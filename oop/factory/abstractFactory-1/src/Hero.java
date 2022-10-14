import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Hero {

    private int hp = 10;
    private int strength;
    private int dexterity;
    private Weapon weapon;
    private Armor armor;

    public Hero(HeroBearingFactory bearingFactory) {
        weapon = bearingFactory.createWeapon();
        armor = bearingFactory.createArmor();
        strength = determineAbilityScore();
        dexterity = determineAbilityScore();
    }

    private int determineAbilityScore(){
        int[] dices = new int[4];
        for (int i = 0; i < dices.length; i++) {
            dices[i] = ThreadLocalRandom.current().nextInt(6) + 1;
        }
        Arrays.sort(dices);
        int sum = 0;
        for (int i = 0; i < dices.length; i++) {
            sum += dices[i];
        }
        return sum;
    }

    private int computeModifier(int ability){
        int modifier = ability - 10;
        if(modifier < 0) --modifier;
        return modifier/2;
    }

    public int getAttackModifier() {
        return computeModifier(strength);
    }

    public int getArmorClass() {
        return armor.armorClass(computeModifier(dexterity));
    }

    public void updateHP(int point) {
        hp += point;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    //debug
    @Override
    public String toString() {
        return String.format("HP: %d, Strength: %d, Dexterity: %d, Weapon: %s, Armor: %s",
                hp, strength, dexterity, weapon.getClass().getName(), armor.getClass().getName());
    }


}
