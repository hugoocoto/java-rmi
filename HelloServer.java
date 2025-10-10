
import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class HelloServer extends UnicastRemoteObject implements HelloInterface {

    public HelloServer() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;
        try {
            System.out.print("Enter the RMIregistry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            HelloInterface exportedObj = new HelloServer();
            registryURL = "rmi://localhost:" + portNum + "/hello";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server registered.  Registry currently contains:");

            listRegistry(registryURL);
            System.out.println("Hello Server ready.");
        } catch (Exception re) {
            System.out.println("Exception in HelloServer.main: " + re);
        }
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();

        } catch (RemoteException e) {

            System.out.println("RMI registry cannot be located at port "
                    + RMIPortNum);
            Registry registry
                    = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println(
                    "RMI registry created at port " + RMIPortNum);
        }
    }

    private static void listRegistry(String registryURL)
            throws RemoteException, MalformedURLException {
        System.out.println("Registry " + registryURL + " contains: ");
        String[] names = Naming.list(registryURL);
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
    }

    @Override
    public long sayHello(long n) throws RemoteException {
        long m = 0;
        double x, y;
        // Date d = new Date();
        // Random r = new Random(d.getTime());
        // Al lanzar muchos clientes a la vez algunos coinciden en la semilla
        for (long i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();
            if (x * x + y * y <= 1.0) {
                ++m;
            }
        }
        System.out.println("Returning m=" + m);
        return m;
    }

}
