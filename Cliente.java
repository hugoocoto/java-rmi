
import java.io.*;
import java.rmi.Naming;
import java.util.ArrayList;

/* Autor: Hugo Coto Florez */

public class Cliente {

    private static ArrayList<String> registryURLs = new ArrayList<>();
    private static ArrayList<Integer> solutions = new ArrayList<>();
    private static Integer n;

    public static void main(String args[]) {
        try {
            String hostName;
            String portNum;

            InputStreamReader is = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);

            int nn = 0;
            int iters = 0;
            while (true) {
                try {
                    System.out.print("Number of iterations: ");
                    iters = Integer.parseInt(br.readLine());
                    System.out.print("Number of servers: ");
                    nn = Integer.parseInt(br.readLine());
                    if (iters % nn != 0) {
                        System.out.println("Iterations are not divisible by servers!");
                        continue;
                    }
                    n = iters / nn;
                    break;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            for (int i = 0; i < nn; i++) {
                System.out.print("[" + i + "] Host: ");
                hostName = br.readLine();
                if (hostName.isEmpty()) {
                    hostName = "localhost";
                }
                System.out.print("[" + i + "] Port: ");
                portNum = br.readLine();
                if (portNum.isEmpty()) {
                    portNum = "8080";
                }
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

        pi = 4 * (double) pi / ((double) n * solutions.size());

        System.out.println("Pi: " + pi);
    }

    private static void launch_a_server(String registryURL) {
        try {
            IMontecarlo h = (IMontecarlo) Naming.lookup(registryURL);
            solutions.add(h.getMontecarlo(n));

        } catch (Exception e) {
            System.out.println("Exception in: " + e);
        }
    }
}
