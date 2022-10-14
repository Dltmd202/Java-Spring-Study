import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void fight(Hero attacker, Hero defender) {
        int dice = ThreadLocalRandom.current().nextInt(20)+1;
        int attackPoint = dice+attacker.getAttackModifier();
        int ac = defender.getArmorClass();
        System.out.printf("주사위: %d, 공격 포인트: %d, 상대방 AC: %d%n",
                dice, attackPoint, ac);
        if(dice==1) return;
        if(dice==20 || attackPoint>ac) {
            int damage = attacker.getWeapon().damage();
            System.out.printf("damage = %d%n", damage);
            defender.updateHP(-damage);
        }
    }

    public static void main(String[] args) {
        Hero hero1 = new Hero(new ArcherBearingFactory());
        Hero hero2 = new Hero(new PaladinBearingFactory());
        Hero hero3 = new Hero(new ThiefBearingFactory());
        System.out.println(hero1);
        System.out.println(hero2);
        System.out.println(hero3);

        fight(hero1, hero2);
        System.out.println(hero1);
        System.out.println(hero2);
        fight(hero2, hero1);
        System.out.println(hero1);
        System.out.println(hero2);
    }
}