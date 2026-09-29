import java.net.*;
import java.util.Scanner;

public class Chat_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6702);
        Scanner scanner = new Scanner(System.in);
        byte[] buffer = new byte[2048];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(),0,packet.getLength());
            System.out.println("Client: " + msg);
            if (msg.equalsIgnoreCase("exit")) break;
            System.out.print("Server: ");
            String reply = scanner.nextLine();
            byte[] data=reply.getBytes();
            socket.send(new DatagramPacket(data,data.length,packet.getAddress(),packet.getPort()));
            if (reply.equalsIgnoreCase("exit")) break;
        }
        socket.close(); scanner.close();
    }
}
