import java.net.*;
import java.util.Scanner;

public class Chat_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Client: ");
            String msg = scanner.nextLine();
            byte[] data=msg.getBytes();
            socket.send(new DatagramPacket(data,data.length,address,6702));
            if (msg.equalsIgnoreCase("exit")) break;
            byte[] buffer=new byte[2048]; DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
            socket.receive(packet);
            String reply=new String(packet.getData(),0,packet.getLength());
            System.out.println("Server: " + reply);
            if (reply.equalsIgnoreCase("exit")) break;
        }
        socket.close(); scanner.close();
    }
}
