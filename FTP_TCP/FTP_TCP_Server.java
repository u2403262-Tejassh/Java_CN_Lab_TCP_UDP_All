import java.io.*;
import java.net.*;

public class FTP_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6401);
        new File("server").mkdirs();
        System.out.println("FTP TCP Server waiting...");
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("220 FTP Server Ready");

        while (true) {
            String command = in.readLine();
            if (command == null) break;
            System.out.println("Client: " + command);

            if (command.equals("LIST")) {
                File folder = new File("server");
                File[] files = folder.listFiles();
                if (files != null) for (File file : files) out.println(file.getName());
                out.println("END");
            } else if (command.startsWith("UPLOAD ")) {
                String name = command.substring(7);
                out.println("READY");
                BufferedWriter fileOut = new BufferedWriter(new FileWriter(new File("server", name)));
                String line;
                while (!(line = in.readLine()).equals("END")) {
                    fileOut.write(line);
                    fileOut.newLine();
                }
                fileOut.close();
                out.println("250 File uploaded successfully");
            } else if (command.startsWith("DOWNLOAD ")) {
                String name = command.substring(9);
                File file = new File("server", name);
                if (!file.exists()) {
                    out.println("404 File not found");
                } else {
                    out.println("READY");
                    BufferedReader fileIn = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = fileIn.readLine()) != null) out.println(line);
                    out.println("END");
                    fileIn.close();
                }
            } else if (command.equals("QUIT")) {
                out.println("221 Goodbye");
                break;
            } else out.println("500 Invalid command");
        }
        socket.close();
        serverSocket.close();
    }
}
