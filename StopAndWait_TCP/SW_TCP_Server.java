import java.io.*;
import java.net.*;
import java.util.Random;

public class SW_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6101);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Random random = new Random();
        double ackLoss = 0.30;
        int expected = 0;

        out.println("READY");
        while (true) {
            String frame = in.readLine();
            if (frame == null || frame.equals("QUIT")) break;

            int number = Integer.parseInt(frame.replace("FRAME ", ""));
            System.out.println("Received: " + frame);

            if (number == expected) {
                System.out.println("Frame " + number + " accepted");
                expected = (expected + 1) % 2;
            } else {
                System.out.println("Duplicate Frame " + number + " discarded");
            }

            String ack = "ACK " + expected;
            if (random.nextDouble() < ackLoss) {
                System.out.println(ack + " lost");
            } else {
                out.println(ack);
                System.out.println(ack + " sent");
            }
        }
        socket.close();
        serverSocket.close();
    }
}
