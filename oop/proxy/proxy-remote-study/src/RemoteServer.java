import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RemoteServer {
    public static void main(String[] args){
        try {
            Hello remoteObject = new HelloRemoteImpl();
            Naming.rebind("rmi://localhost:11099/RemoteHello", remoteObject);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
