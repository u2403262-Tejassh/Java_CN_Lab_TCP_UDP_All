import java.io.*;
import java.net.*;
import java.util.Random;

public class SR_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6301);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Random random = new Random();
        double ackLoss = 0.30;
        int expected = 0;
        int window = 4;
        boolean[] received = new boolean[100];

        while (true) {
            String frame = in.readLine();
            if (frame == null || frame.equals("QUIT")) break;
            int number = Integer.parseInt(frame.replace("FRAME ", ""));
            System.out.println("Received: " + frame);

            if (number >= expected && number < expected + window) {
                if (!received[number]) {
                    received[number] = true;
                    System.out.println("Frame " + number + " buffered");
                    if (number > expected && !received[expected]) {
                        out.println("NAK " + expected);
                        System.out.println("NAK " + expected + " sent");
                    }
                    int oldExpected = expected;
                    while (expected < received.length && received[expected]) {
                        System.out.println("Frame " + expected + " delivered");
                        expected++;
                    }
                    if (expected > oldExpected) {
                        String ack = "ACK " + expected;
                        if (random.nextDouble() < ackLoss) {
                            System.out.println(ack + " lost");
                        } else {
                            out.println(ack);
                            System.out.println(ack + " sent");
                        }
                    }
                } else {
                    System.out.println("Duplicate Frame " + number + " received");
                    out.println("ACK " + expected);
                }
            } else if (number < expected) {
                System.out.println("Old duplicate Frame " + number + " received");
                out.println("ACK " + expected);
                System.out.println("ACK " + expected + " sent again");
            } else {
                System.out.println("Frame " + number + " outside window");
            }
        }
        socket.close();
        serverSocket.close();
    }
}
