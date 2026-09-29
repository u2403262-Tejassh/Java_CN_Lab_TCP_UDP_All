import java.net.*;
import java.util.Scanner;

public class DVR_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter neighbor routing vector (example: 2 0 1): ");
        String vector = scanner.nextLine();
        byte[] data = vector.getBytes();

        DatagramPacket packet = new DatagramPacket(
                data, data.length, address, 6002);
        socket.send(packet);

        byte[] buffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.receive(response);

        System.out.println(new String(response.getData(), 0, response.getLength()));

        socket.close();
        scanner.close();
    }
}
