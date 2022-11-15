import java.rmi.Naming;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 원격 프록시
 * RMI 서버
 */
public class RemoteServer {
	public static void main(String[] args) {
		try{
			Hello remoteObject = new HelloRemoteImpl();
			Naming.rebind("rmi://localhost:11099/RemoteHello", remoteObject);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
