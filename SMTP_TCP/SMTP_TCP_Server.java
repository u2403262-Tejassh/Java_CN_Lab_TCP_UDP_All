import java.io.*;
import java.net.*;

public class SMTP_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6501);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("220 SMTP Server Ready");

        while (true) {
            String command = in.readLine();
            if (command == null) break;
            System.out.println("Client: " + command);
            if (command.startsWith("HELO")) out.println("250 Hello");
            else if (command.startsWith("MAIL FROM")) out.println("250 Sender OK");
            else if (command.startsWith("RCPT TO")) out.println("250 Receiver OK");
            else if (command.equals("DATA")) {
                out.println("354 Start mail input; end with .");
                String line;
                while (!(line = in.readLine()).equals(".")) System.out.println("Mail: " + line);
                out.println("250 Message accepted");
            } else if (command.equals("QUIT")) { out.println("221 Bye"); break; }
            else out.println("500 Command not recognized");
        }
        socket.close(); serverSocket.close();
    }
}
