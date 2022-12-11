package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloRemoteImpl extends UnicastRemoteObject implements Hello {
    private static final long serialVersionUID = 7817098198941092918L;
    public HelloRemoteImpl() throws RemoteException {}
    @Override
    public String sayHello(String name) throws RemoteException {
        return name+"님, 원격에서 주는 황홀한 문자열입니다!!!";
    }
}
