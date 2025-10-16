
import java.rmi.*;

public interface IMontecarlo extends Remote {

    public Integer getMontecarlo(Integer n)
            throws java.rmi.RemoteException;

}
