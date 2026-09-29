import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class SW_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        double frameLoss = 0.30;

        System.out.print("Enter number of frames: ");
        int n = scanner.nextInt();
        int sequence = 0;

        for (int i = 0; i < n; i++) {
            boolean done = false;
            while (!done) {
                String frame = "FRAME " + sequence;
                byte[] data = frame.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, address, 6102);

                if (random.nextDouble() < frameLoss) {
                    System.out.println(frame + " lost");
                } else {
                    socket.send(packet);
                    System.out.println(frame + " sent");
                }

                socket.setSoTimeout(2000);
                try {
                    byte[] ackData = new byte[100];
                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length);
                    socket.receive(ackPacket);
                    String ack = new String(ackPacket.getData(), 0, ackPacket.getLength());
                    System.out.println("Received: " + ack);
                    int ackNo = Integer.parseInt(ack.replace("ACK ", ""));
                    if (ackNo == (sequence + 1) % 2) done = true;
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout! Resending " + frame);
                }
            }
            sequence = (sequence + 1) % 2;
        }

        byte[] quit = "QUIT".getBytes();
        socket.send(new DatagramPacket(quit, quit.length, address, 6102));
        socket.close();
        scanner.close();
    }
}
