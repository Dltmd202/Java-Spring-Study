package v6;

public class Quackologist implements Observer{
    @Override
    public void update(QuackObservable duck) {
        System.out.println("오리학자: " + duck + " 방금 꽥꽥함");
    }
}
