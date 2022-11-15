import java.rmi.RemoteException;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 원격 프록시
 * 구체적 대상 객체
 */
public class HelloLocalImpl implements Hello {

	public HelloLocalImpl(){}
	@Override
	public String sayHello(String name) throws RemoteException {
		return name+"님, 로컬에서 주는 재미없는 문자열입니다!!!: ";
	}

}
