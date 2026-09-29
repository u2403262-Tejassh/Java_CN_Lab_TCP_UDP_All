import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Chat_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6701);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Server: " + in.readLine());
        while (true) {
            System.out.print("Client: ");
            String msg = scanner.nextLine(); out.println(msg);
            if (msg.equalsIgnoreCase("exit")) break;
            String reply = in.readLine();
            System.out.println("Server: " + reply);
            if (reply.equalsIgnoreCase("exit")) break;
        }
        socket.close(); scanner.close();
    }
}
