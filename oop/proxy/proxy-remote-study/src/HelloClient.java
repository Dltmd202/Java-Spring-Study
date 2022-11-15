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
                        (Hello)Naming.lookup("rmi://localhost:11099/RemoteHello");
                System.out.println("Server Connected");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else
            service = new HelloLocalImpl();
        return service;
    }

    public static void main(String[] args) throws RemoteException{
        for(int i=0; i<5; i++) {
            Hello object = getRemoteOrLocalObject();
            String s = object.sayHello("길동");
            System.out.println(s);
        }
    }
}
