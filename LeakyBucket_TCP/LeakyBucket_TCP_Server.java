import java.io.*;
import java.net.*;

public class LeakyBucket_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6601);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String config = in.readLine(); // capacity leakRate
        String[] c = config.split(" ");
        int capacity = Integer.parseInt(c[0]);
        int leakRate = Integer.parseInt(c[1]);
        int bucket = 0;
        System.out.println("Bucket capacity = " + capacity + ", leak rate = " + leakRate);

        String line;
        while (!(line = in.readLine()).equals("END")) {
            int packet = Integer.parseInt(line);
            if (bucket + packet > capacity) {
                out.println("Packet " + packet + " DROPPED (bucket overflow)");
            } else {
                bucket += packet;
                out.println("Packet " + packet + " accepted, bucket = " + bucket);
                int leaked = Math.min(leakRate, bucket);
                bucket -= leaked;
                out.println("Leaked " + leaked + ", bucket = " + bucket);
            }
        }
        out.println("DONE");
        socket.close(); serverSocket.close();
    }
}
