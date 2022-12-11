package remote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;

public class HelloClient {
    public static Hello getRemoteOrLocalObject(){
        Hello service = null;
        if(ThreadLocalRandom.current().nextBoolean()){
            try {
                service =
                        (Hello)Naming.lookup("rmi://220.68.82.24:11099/RemoteHello");
                System.out.println("Server Connected");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else service = new
    }
}
