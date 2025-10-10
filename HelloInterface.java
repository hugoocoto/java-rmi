
import java.rmi.*;

public interface HelloInterface extends Remote {

    public long sayHello(long n)
            throws java.rmi.RemoteException;

}
