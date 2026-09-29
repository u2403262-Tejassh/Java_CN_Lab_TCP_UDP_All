import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LeakyBucket_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6601);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter bucket capacity: "); int capacity = scanner.nextInt();
        System.out.print("Enter leak rate: "); int leakRate = scanner.nextInt();
        System.out.print("Enter number of packets: "); int n = scanner.nextInt();
        out.println(capacity + " " + leakRate);
        for (int i = 0; i < n; i++) {
            System.out.print("Enter packet size: ");
            out.println(scanner.nextInt());
            System.out.println(in.readLine());
            System.out.println(in.readLine());
        }
        out.println("END");
        System.out.println(in.readLine());
        socket.close(); scanner.close();
    }
}
