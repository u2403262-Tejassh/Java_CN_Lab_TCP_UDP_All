import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class SW_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6101);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        double frameLoss = 0.30;

        System.out.println("Server: " + in.readLine());
        System.out.print("Enter number of frames: ");
        int n = scanner.nextInt();
        int sequence = 0;

        for (int i = 0; i < n; i++) {
            boolean done = false;
            while (!done) {
                String frame = "FRAME " + sequence;
                if (random.nextDouble() < frameLoss) {
                    System.out.println(frame + " lost");
                } else {
                    out.println(frame);
                    System.out.println(frame + " sent");
                }
                socket.setSoTimeout(2000);
                try {
                    String ack = in.readLine();
                    if (ack == null) break;
                    System.out.println("Received: " + ack);
                    int ackNo = Integer.parseInt(ack.replace("ACK ", ""));
                    if (ackNo == (sequence + 1) % 2) done = true;
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout! Resending " + frame);
                }
            }
            sequence = (sequence + 1) % 2;
        }
        out.println("QUIT");
        socket.close();
        scanner.close();
    }
}
