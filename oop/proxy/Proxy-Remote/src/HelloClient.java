import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 원격 프록시
 * 테스트 프로그램
 */
public class HelloClient {
	public static Hello getRemoteOrLocalObject() {
		Hello service = null;
		if(ThreadLocalRandom.current().nextBoolean()) {
			try{
				service = 
					//(Hello)Naming.lookup("rmi://127.0.0.1:11099/RemoteHello");
					(Hello)Naming.lookup("rmi://220.68.82.24:11099/RemoteHello");
					System.out.println("Server Connected");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		else service = new HelloLocalImpl();
		return service;
	}
	public static void main(String[] args) throws RemoteException {
		for(int i=0; i<5; i++) {
			Hello object = getRemoteOrLocalObject();
			String s = object.sayHello("길동");
			System.out.println(s);
		}
	}

}
