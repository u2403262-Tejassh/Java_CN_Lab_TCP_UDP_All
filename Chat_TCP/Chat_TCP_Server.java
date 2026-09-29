import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Chat_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6701);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        out.println("Connected to chat server");

        while (true) {
            String msg = in.readLine();
            if (msg == null || msg.equalsIgnoreCase("exit")) break;
            System.out.println("Client: " + msg);
            System.out.print("Server: ");
            String reply = scanner.nextLine();
            out.println(reply);
            if (reply.equalsIgnoreCase("exit")) break;
        }
        socket.close(); serverSocket.close(); scanner.close();
    }
}
