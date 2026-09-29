import java.net.*;

public class DVR_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6002);
        System.out.println("DVR UDP Server waiting...");

        int[] distance = {0, 2, 7};
        String[] node = {"A", "B", "C"};

        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.receive(packet);

        String line = new String(packet.getData(), 0, packet.getLength());
        String[] p = line.trim().split("\\s+");
        int[] neighbor = new int[p.length];
        for (int i = 0; i < p.length; i++) {
            neighbor[i] = Integer.parseInt(p[i]);
        }

        int linkCost = 2;
        for (int i = 0; i < distance.length && i < neighbor.length; i++) {
            int throughB = linkCost + neighbor[i];
            if (throughB < distance[i]) {
                distance[i] = throughB;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("Updated routing table\n");
        for (int i = 0; i < distance.length; i++) {
            result.append(node[i]).append(" ").append(distance[i]).append("\n");
        }

        byte[] outData = result.toString().getBytes();
        DatagramPacket response = new DatagramPacket(
                outData, outData.length, packet.getAddress(), packet.getPort());
        socket.send(response);
        socket.close();
    }
}
