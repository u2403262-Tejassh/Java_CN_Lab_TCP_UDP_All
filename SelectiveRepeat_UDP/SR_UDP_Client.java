import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class SR_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
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
                sendFrame(socket, address, frames[next], random, frameLoss);
                next++;
            }
            socket.setSoTimeout(2000);
            try {
                byte[] data = new byte[100];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                String response = new String(packet.getData(), 0, packet.getLength());
                System.out.println(response + " received");
                if (response.startsWith("ACK ")) {
                    int ackNo = Integer.parseInt(response.replace("ACK ", ""));
                    for (int i = base; i < ackNo && i < n; i++) acked[i] = true;
                    while (base < n && acked[base]) base++;
                } else if (response.startsWith("NAK ")) {
                    int nak = Integer.parseInt(response.replace("NAK ", ""));
                    if (nak >= 0 && nak < n && !acked[nak]) sendFrame(socket, address, frames[nak], random, frameLoss);
                }
            } catch (SocketTimeoutException e) {
                if (base < next) {
                    System.out.println("Timeout! Retransmitting oldest Frame " + base);
                    sendFrame(socket, address, frames[base], random, frameLoss);
                }
            }
        }
        System.out.println("All frames sent successfully.");
        byte[] quit = "QUIT".getBytes();
        socket.send(new DatagramPacket(quit, quit.length, address, 6302));
        socket.close();
        scanner.close();
    }

    static void sendFrame(DatagramSocket socket, InetAddress address, String frame, Random random, double loss) throws Exception {
        byte[] data = frame.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, address, 6302);
        if (random.nextDouble() < loss) System.out.println(frame + " lost");
        else { socket.send(packet); System.out.println(frame + " sent"); }
    }
}
