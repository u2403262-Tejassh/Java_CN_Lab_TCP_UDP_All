import java.net.*;
import java.util.Scanner;

public class LeakyBucket_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter bucket capacity: "); int capacity=scanner.nextInt();
        System.out.print("Enter leak rate: "); int leakRate=scanner.nextInt();
        System.out.print("Enter number of packets: "); int n=scanner.nextInt();
        send(socket,address,""+capacity+" "+leakRate); System.out.println(receive(socket));
        for(int i=0;i<n;i++){
            System.out.print("Enter packet size: ");
            send(socket,address,""+scanner.nextInt());
            System.out.println(receive(socket));
        }
        send(socket,address,"END"); System.out.println(receive(socket));
        socket.close(); scanner.close();
    }
    static void send(DatagramSocket socket,InetAddress address,String text)throws Exception{
        byte[] data=text.getBytes(); socket.send(new DatagramPacket(data,data.length,address,6602));
    }
    static String receive(DatagramSocket socket)throws Exception{
        byte[] data=new byte[2048]; DatagramPacket p=new DatagramPacket(data,data.length);
        socket.receive(p); return new String(p.getData(),0,p.getLength());
    }
}
