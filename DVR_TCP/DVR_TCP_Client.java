import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DVR_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6001);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println(in.readLine());
        System.out.print("Enter neighbor routing vector (example: 2 0 1): ");
        String vector = scanner.nextLine();
        out.println(vector);

        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("END")) {
                break;
            }
            System.out.println(line);
        }

        socket.close();
        scanner.close();
    }
}
