import java.lang.reflect.Proxy;

public class Main {
    public static PersonBean getOwnerProxy(PersonBean personBean){
        return (PersonBean) Proxy.newProxyInstance(
                PersonBean.class.getClassLoader(),
                PersonBean.class.getInterfaces(),
                new OwnerInvocationHandler(personBean)
        );
    }

    public static PersonBean getNonOwnerProxy(PersonBean personBean){
        return (PersonBean) Proxy.newProxyInstance(
                PersonBean.class.getClassLoader(),
                PersonBean.class.getInterfaces(),
                new NonOwnerInvocationHandler(personBean)
        );
    }
    public static void main(String[] args) {
        PersonBean person1 = new PersonBeanImpl();
        PersonBean ownerProxy = getOwnerProxy(person1);
        try {
            ownerProxy.setName("이승환");
            ownerProxy.setGender(PersonBean.Gender.MALE);
            ownerProxy.setInterest("음악");
            ownerProxy.setHotOrNotRating(10);
        } catch (Exception e){
            System.out.println("본인 평판을 설정할 수 없음");
        }

        PersonBean nonOwnerProxy = getNonOwnerProxy(person1);
        try {
            nonOwnerProxy.setHotOrNotRating(10);
            nonOwnerProxy.setInterest("축구");
        } catch (Exception e){
            System.out.println("다른 사용자의 과심사항을 수정할 수 없음");
        }
    }
}