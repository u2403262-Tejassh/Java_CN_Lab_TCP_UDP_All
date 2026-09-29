import java.io.*;
import java.net.*;

public class DVR_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6001);
        System.out.println("DVR TCP Server waiting...");
        Socket socket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Local distances from Router A to A, B, C
        int[] distance = {0, 2, 7};
        String[] node = {"A", "B", "C"};

        out.println("Neighbor vector received format: destination distances separated by spaces");
        String line = in.readLine();
        String[] p = line.trim().split("\\s+");

        // Client sends B's vector. Link cost A-B is 2.
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

        out.println("Updated routing table");
        for (int i = 0; i < distance.length; i++) {
            out.println(node[i] + " " + distance[i]);
        }
        out.println("END");

        socket.close();
        serverSocket.close();
    }
}
