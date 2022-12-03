import java.rmi.RemoteException;

public class HelloLocalImpl implements Hello{
    public HelloLocalImpl() {
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return name+"님, 로컬에서 주는 재미없는 문자열입니다!!!: ";
    }
}
