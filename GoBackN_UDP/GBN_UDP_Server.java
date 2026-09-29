import java.net.*;
import java.util.Random;

public class GBN_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6202);
        Random random = new Random();
        double ackLoss = 0.30;
        int expected = 0;
        byte[] buffer = new byte[100];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String frame = new String(packet.getData(), 0, packet.getLength());
            if (frame.equals("QUIT")) break;
            int number = Integer.parseInt(frame.replace("FRAME ", ""));
            System.out.println("Received: " + frame);

            if (number == expected) {
                System.out.println("Frame " + number + " accepted");
                expected++;
                sendAck(socket, packet, expected, random, ackLoss, false);
            } else if (number < expected) {
                System.out.println("Duplicate Frame " + number + " discarded");
                sendAck(socket, packet, expected, random, ackLoss, true);
            } else {
                System.out.println("Out of order Frame " + number + " discarded");
            }
        }
        socket.close();
    }

    static void sendAck(DatagramSocket socket, DatagramPacket packet, int number,
                        Random random, double loss, boolean repeated) throws Exception {
        String ack = "ACK " + number;
        if (random.nextDouble() < loss) {
            System.out.println(ack + " lost");
            return;
        }
        byte[] data = ack.getBytes();
        DatagramPacket p = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
        socket.send(p);
        System.out.println(ack + (repeated ? " sent again" : " sent"));
    }
}
