import java.net.*;
import java.util.Scanner;

public class Calculator_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket=new DatagramSocket(); InetAddress address=InetAddress.getByName("localhost"); Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.print("Enter expression (example: 5 + 3): "); String e=scanner.nextLine();
            byte[] data=e.getBytes(); socket.send(new DatagramPacket(data,data.length,address,6802));
            if(e.equalsIgnoreCase("exit")) break;
            byte[] buffer=new byte[1024]; DatagramPacket p=new DatagramPacket(buffer,buffer.length); socket.receive(p);
            System.out.println("Result: "+new String(p.getData(),0,p.getLength()));
        }
        socket.close(); scanner.close();
    }
}
