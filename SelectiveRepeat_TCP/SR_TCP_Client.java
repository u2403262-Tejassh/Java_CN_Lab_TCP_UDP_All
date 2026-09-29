import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class SR_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6301);
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
        boolean[] acked = new boolean[n];
        for (int i = 0; i < n; i++) frames[i] = "FRAME " + i;

        int base = 0, next = 0;
        while (base < n) {
            while (next < n && next < base + window) {
                sendFrame(out, frames[next], random, frameLoss);
                next++;
            }
            socket.setSoTimeout(2000);
            try {
                String response = in.readLine();
                if (response == null) break;
                System.out.println(response + " received");
                if (response.startsWith("ACK ")) {
                    int ackNo = Integer.parseInt(response.replace("ACK ", ""));
                    for (int i = base; i < ackNo && i < n; i++) acked[i] = true;
                    while (base < n && acked[base]) base++;
                } else if (response.startsWith("NAK ")) {
                    int nak = Integer.parseInt(response.replace("NAK ", ""));
                    if (nak >= 0 && nak < n && !acked[nak]) {
                        sendFrame(out, frames[nak], random, frameLoss);
                    }
                }
            } catch (SocketTimeoutException e) {
                if (base < next) {
                    System.out.println("Timeout! Retransmitting oldest Frame " + base);
                    sendFrame(out, frames[base], random, frameLoss);
                }
            }
        }
        out.println("QUIT");
        socket.close();
        scanner.close();
    }

    static void sendFrame(PrintWriter out, String frame, Random random, double loss) {
        if (random.nextDouble() < loss) {
            System.out.println(frame + " lost");
        } else {
            out.println(frame);
            System.out.println(frame + " sent");
        }
    }
}
