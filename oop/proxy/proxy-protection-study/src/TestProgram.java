import java.lang.reflect.Proxy;

public class TestProgram {

    public static PersonBean getOwnerProxy(PersonBean personBean){
        return (PersonBean) Proxy.newProxyInstance(
                personBean.getClass().getClassLoader(),
                personBean.getClass().getInterfaces(),
                new OwnerInvocationHandler(personBean));
    }

    public static PersonBean getNonOwnerProxy(PersonBean personBean){
        return (PersonBean) Proxy.newProxyInstance(
                personBean.getClass().getClassLoader(),
                personBean.getClass().getInterfaces(),
                new NonOwnerInvocationHandler(personBean));
    }

    public static void main(String[] args) {
        PersonBean sh = new PersonBeanImpl();
        PersonBean ownerProxy = getOwnerProxy(sh);
        try {
            ownerProxy.setName("sh");
            ownerProxy.setGender(PersonBean.Gender.MALE);
            ownerProxy.setInterest("음악");
            ownerProxy.setHotOrNotRating(10);
        } catch (Exception e){
            System.out.println("본인의 평판을 설정할 수 없음");
        }

        PersonBean nonOwnerProxy = getNonOwnerProxy(sh);

        try {
            nonOwnerProxy.setHotOrNotRating(10);
            ownerProxy.setInterest("축구");
        } catch (Exception e){
            System.out.println("다른 사용자의 관심사항을 수정할 수 없음");
        }
    }
}
