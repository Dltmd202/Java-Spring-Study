package turkey;

public class BlackTurkey implements Turkey{
    @Override
    public void gobble() {
        System.out.println("블랙칠면조 고블 고블");

    }
    @Override
    public void fly() {
        System.out.println("푸드덕 푸드덕");
    }
}
