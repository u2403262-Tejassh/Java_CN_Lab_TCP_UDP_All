import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class GBN_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6201);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        double frameLoss = 0.30;

        System.out.print("Enter number of frames: ");
        int n = scanner.nextInt();
        System.out.print("Enter window size: ");
        int window = scanner.nextInt();
        String[] frames = new String[n];
        for (int i = 0; i < n; i++) frames[i] = "FRAME " + i;

        int base = 0, next = 0;
        while (base < n) {
            while (next < n && next < base + window) {
                if (random.nextDouble() < frameLoss) {
                    System.out.println(frames[next] + " lost");
                } else {
                    out.println(frames[next]);
                    System.out.println(frames[next] + " sent");
                }
                next++;
            }
            socket.setSoTimeout(2000);
            try {
                String ack = in.readLine();
                if (ack == null) break;
                System.out.println(ack + " received");
                int ackNo = Integer.parseInt(ack.replace("ACK ", ""));
                if (ackNo > base && ackNo <= next) base = ackNo;
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout! Go back to Frame " + base);
                for (int i = base; i < next; i++) {
                    if (random.nextDouble() < frameLoss) {
                        System.out.println(frames[i] + " retransmission lost");
                    } else {
                        out.println(frames[i]);
                        System.out.println(frames[i] + " retransmitted");
                    }
                }
            }
        }
        System.out.println("All frames sent successfully.");
        out.println("QUIT");
        socket.close();
        scanner.close();
    }
}
