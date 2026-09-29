import java.net.*;

public class SR_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6302);
        int expected = 0, window = 4;
        boolean[] received = new boolean[100];
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String frame = new String(packet.getData(), 0, packet.getLength());
            if (frame.equals("QUIT")) break;
            int number = Integer.parseInt(frame.replace("FRAME ", ""));
            System.out.println("Received: " + frame);

            if (number >= expected && number < expected + window) {
                if (!received[number]) {
                    received[number] = true;
                    System.out.println("Frame " + number + " buffered");
                    if (number > expected && !received[expected]) send(socket, packet, "NAK " + expected);
                    int old = expected;
                    while (expected < received.length && received[expected]) {
                        System.out.println("Frame " + expected + " delivered");
                        expected++;
                    }
                    if (expected > old) send(socket, packet, "ACK " + expected);
                } else {
                    System.out.println("Duplicate Frame " + number + " received");
                    send(socket, packet, "ACK " + expected);
                }
            } else if (number < expected) {
                System.out.println("Old duplicate Frame " + number + " received");
                send(socket, packet, "ACK " + expected);
            } else {
                System.out.println("Frame " + number + " outside window");
            }
        }
        socket.close();
    }

    static void send(DatagramSocket socket, DatagramPacket original, String text) throws Exception {
        byte[] data = text.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, original.getAddress(), original.getPort());
        socket.send(packet);
        System.out.println(text + " sent");
    }
}
