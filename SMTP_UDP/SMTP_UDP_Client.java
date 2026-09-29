import java.net.*;
import java.util.Scanner;

public class SMTP_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        send(socket,address,"HELO localhost"); System.out.println(receive(socket));
        System.out.print("Sender email: "); String from = scanner.nextLine();
        send(socket,address,"MAIL FROM:<" + from + ">"); System.out.println(receive(socket));
        System.out.print("Receiver email: "); String to = scanner.nextLine();
        send(socket,address,"RCPT TO:<" + to + ">"); System.out.println(receive(socket));
        send(socket,address,"DATA"); System.out.println(receive(socket));
        System.out.print("Subject: "); String subject = scanner.nextLine();
        System.out.print("Message: "); String message = scanner.nextLine();
        send(socket,address,"Subject: " + subject); System.out.println(receive(socket));
        send(socket,address,message); System.out.println(receive(socket));
        send(socket,address,"."); System.out.println(receive(socket));
        send(socket,address,"QUIT"); System.out.println(receive(socket));

        socket.close(); scanner.close();
    }
    static void send(DatagramSocket socket, InetAddress address, String text) throws Exception {
        byte[] data=text.getBytes(); socket.send(new DatagramPacket(data,data.length,address,6502));
    }
    static String receive(DatagramSocket socket) throws Exception {
        byte[] data=new byte[2048]; DatagramPacket p=new DatagramPacket(data,data.length);
        socket.receive(p); return new String(p.getData(),0,p.getLength());
    }
}
