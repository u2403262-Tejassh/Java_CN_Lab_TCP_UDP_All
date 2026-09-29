import java.net.*;

public class SMTP_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6502);
        byte[] buffer = new byte[2048];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String command = new String(packet.getData(), 0, packet.getLength());
            if (command.equals("QUIT")) { send(socket,packet,"221 Bye"); break; }
            System.out.println("Client: " + command);
            String reply;
            if (command.startsWith("HELO")) reply = "250 Hello";
            else if (command.startsWith("MAIL FROM")) reply = "250 Sender OK";
            else if (command.startsWith("RCPT TO")) reply = "250 Receiver OK";
            else if (command.equals("DATA")) reply = "354 Ready for message";
            else if (command.equals(".")) reply = "250 Message accepted";
            else reply = "250 Received";
            send(socket,packet,reply);
        }
        socket.close();
    }
    static void send(DatagramSocket socket, DatagramPacket original, String text) throws Exception {
        byte[] data = text.getBytes();
        socket.send(new DatagramPacket(data,data.length,original.getAddress(),original.getPort()));
    }
}
