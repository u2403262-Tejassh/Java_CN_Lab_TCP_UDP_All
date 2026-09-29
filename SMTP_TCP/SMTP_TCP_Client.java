import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SMTP_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6501);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Server: " + in.readLine());

        out.println("HELO localhost"); System.out.println(in.readLine());
        System.out.print("Sender email: "); String from = scanner.nextLine();
        out.println("MAIL FROM:<" + from + ">"); System.out.println(in.readLine());
        System.out.print("Receiver email: "); String to = scanner.nextLine();
        out.println("RCPT TO:<" + to + ">"); System.out.println(in.readLine());
        out.println("DATA"); System.out.println(in.readLine());
        System.out.print("Subject: "); String subject = scanner.nextLine();
        System.out.print("Message: "); String message = scanner.nextLine();
        out.println("Subject: " + subject); out.println(message); out.println(".");
        System.out.println(in.readLine());
        out.println("QUIT"); System.out.println(in.readLine());
        socket.close(); scanner.close();
    }
}
