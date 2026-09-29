import java.net.*;
import java.util.Random;

public class SW_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6102);
        Random random = new Random();
        double ackLoss = 0.30;
        int expected = 0;
        byte[] buffer = new byte[100];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String frame = new String(packet.getData(), 0, packet.getLength());
            if (frame.equals("QUIT")) break;
            System.out.println("Received: " + frame);

            int number = Integer.parseInt(frame.replace("FRAME ", ""));
            if (number == expected) {
                System.out.println("Frame " + number + " accepted");
                expected = (expected + 1) % 2;
            } else {
                System.out.println("Duplicate Frame " + number + " discarded");
            }

            String ack = "ACK " + expected;
            byte[] out = ack.getBytes();
            DatagramPacket ackPacket = new DatagramPacket(out, out.length, packet.getAddress(), packet.getPort());
            if (random.nextDouble() < ackLoss) {
                System.out.println(ack + " lost");
            } else {
                socket.send(ackPacket);
                System.out.println(ack + " sent");
            }
        }
        socket.close();
    }
}
