import java.net.*;

public class LeakyBucket_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6602);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String[] c = new String(packet.getData(),0,packet.getLength()).split(" ");
        int capacity = Integer.parseInt(c[0]);
        int leakRate = Integer.parseInt(c[1]);
        int bucket = 0;
        InetAddress address = packet.getAddress(); int port = packet.getPort();
        send(socket,address,port,"READY");

        while (true) {
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String line = new String(packet.getData(),0,packet.getLength());
            if (line.equals("END")) { send(socket,address,port,"DONE"); break; }
            int size = Integer.parseInt(line);
            if (bucket + size > capacity) send(socket,address,port,"Packet " + size + " DROPPED");
            else {
                bucket += size;
                int leaked = Math.min(leakRate,bucket);
                bucket -= leaked;
                send(socket,address,port,"Packet " + size + " accepted; leaked " + leaked + "; bucket = " + bucket);
            }
        }
        socket.close();
    }
    static void send(DatagramSocket socket,InetAddress address,int port,String text)throws Exception{
        byte[] data=text.getBytes(); socket.send(new DatagramPacket(data,data.length,address,port));
    }
}
