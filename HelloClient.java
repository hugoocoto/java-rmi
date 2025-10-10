
import java.io.*;
import java.rmi.Naming;
import java.util.ArrayList;

/* Autor: Hugo Coto Florez
 *
 * Notas: Que puto asco da este codigo 
 * 
 */
public class HelloClient {

    static ArrayList<String> registryURLs = new ArrayList<>();
    static ArrayList<Double> solutions = new ArrayList<>();

    public static void main(String args[]) {
        try {
            String hostName;
            String portNum;

            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);

            int nn = 0;
            while (true) {
                try {
                    System.out.print("Number of servers:");
                    nn = Integer.parseInt(br.readLine());
                    break;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            for (int i = 0; i < nn; i++) {
                System.out.print("Host:");
                hostName = br.readLine();
                System.out.print("Port:");
                portNum = br.readLine();
                registryURLs.add("rmi://" + hostName + ":" + portNum + "/hello");
            }

            report();

        } catch (Exception e) {
            System.out.println("Exception in: " + e);
        }
    }

    private static void report() {
        ArrayList<Thread> threads;
        int threads_n;

        threads_n = registryURLs.size();
        threads = new ArrayList<>(threads_n);

        for (int thread_n = 0; thread_n < threads_n; thread_n++) {
            String registryURL = registryURLs.get(thread_n);
            Thread t = new Thread(() -> launch_a_server(registryURL));
            threads.add(t);
            t.start();
        }

        try {
            for (int thread_n = 0; thread_n < threads_n; thread_n++) {
                threads.get(thread_n).join();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        double pi = 0;
        for (int thread_n = 0; thread_n < solutions.size(); thread_n++) {
            pi += solutions.get(thread_n);
        }
        pi /= solutions.size();
        System.out.println("Pi: " + pi);
    }

    private static void launch_a_server(String registryURL) {
        try {
            HelloInterface h = (HelloInterface) Naming.lookup(registryURL);
            System.out.println("Lookup completed for " + registryURL);

            long n = 1000000;
            long message = h.sayHello(n);
            double pi = 4 * (double) message / (double) n;
            solutions.add(pi);
            System.out.println(String.format("Pi is aprox: %g", pi));

        } catch (Exception e) {
            System.out.println("Exception in: " + e);
        }
    }
}
