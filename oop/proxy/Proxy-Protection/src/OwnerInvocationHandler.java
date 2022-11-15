import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 보호 프록시
 * 동적 프록시를 생성하기 위한 클래스
 * 자신의 객체를 접근할 때 사용할 프록시를 만들어주는 클래스
 */
public class OwnerInvocationHandler implements InvocationHandler {
	private PersonBean person;	
	public OwnerInvocationHandler(PersonBean person) {
		this.person = person;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws IllegalAccessException {
		try{
			if(method.getName().startsWith("get"))
				return method.invoke(person, args);
			else if(method.getName().equals("setHotOrNotRating"))
				throw new IllegalAccessException();
			else if(method.getName().startsWith("set"))
				return method.invoke(person, args);
		}
		catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}

}
