import java.lang.reflect.Proxy;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 보호 프록시
 * 테스트 프로그램
 */
public class TestProgram {
	// 동적 프록시 생성
	// 자신의 객체를 접근할 때 사용할 프록시
	public static PersonBean getOwnerProxy(PersonBean person){
		return (PersonBean)Proxy.newProxyInstance(
				person.getClass().getClassLoader(),
				person.getClass().getInterfaces(),
				new OwnerInvocationHandler(person));
	}

	// 동적 프록시 생성
	// 다른 사용자 객체를 접근할 때 사용할 프록시
	public static PersonBean getNonOwnerProxy(PersonBean person){
		return (PersonBean)Proxy.newProxyInstance(
				person.getClass().getClassLoader(),
				person.getClass().getInterfaces(),
				new NonOwnerInvocationHandler(person));
	}
	
	public static void main(String[] args) {
		PersonBean sangjin = new PersonBeanImpl();
		PersonBean ownerProxy = getOwnerProxy(sangjin);
		try{
			ownerProxy.setName("김상진");
			ownerProxy.setGender(PersonBean.Gender.MALE);
			ownerProxy.setInterest("음악");
			ownerProxy.setHotOrNotRating(10);
		}
		catch(Exception e){
			System.out.println("본인의 평판을 설정할 수 없음");
		}

		PersonBean nonOwnerProxy = getNonOwnerProxy(sangjin);
		try{
			nonOwnerProxy.setHotOrNotRating(10);
			nonOwnerProxy.setInterest("축구");
		}
		catch(Exception e){
			System.out.println("다른 사용자의 관심사항을 수정할 수 없음");
		}

	}
}
