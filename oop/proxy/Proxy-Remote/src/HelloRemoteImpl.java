import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 원격 프록시
 * 원격 프록시 객체
 */
public class HelloRemoteImpl extends UnicastRemoteObject implements Hello {
	private static final long serialVersionUID = 7817098198941092918L;
	public HelloRemoteImpl() throws RemoteException {}
	@Override
	public String sayHello(String name) throws RemoteException {
		return name+"님, 원격에서 주는 황홀한 문자열입니다!!!";
	}
}
