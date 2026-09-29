import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FTP_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6401);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Server: " + in.readLine());

        while (true) {
            System.out.println("1. LIST");
            System.out.println("2. UPLOAD");
            System.out.println("3. DOWNLOAD");
            System.out.println("4. QUIT");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                out.println("LIST");
                String line;
                while (!(line = in.readLine()).equals("END")) System.out.println(line);
            } else if (choice == 2) {
                System.out.print("Enter local file name: ");
                String name = scanner.nextLine();
                File file = new File(name);
                if (!file.exists()) { System.out.println("File not found"); continue; }
                out.println("UPLOAD " + name);
                System.out.println("Server: " + in.readLine());
                BufferedReader fileIn = new BufferedReader(new FileReader(file));
                String line;
                while ((line = fileIn.readLine()) != null) out.println(line);
                out.println("END");
                fileIn.close();
                System.out.println("Server: " + in.readLine());
            } else if (choice == 3) {
                System.out.print("Enter file name: ");
                String name = scanner.nextLine();
                out.println("DOWNLOAD " + name);
                String response = in.readLine();
                if (response.equals("404 File not found")) System.out.println(response);
                else {
                    PrintWriter fileOut = new PrintWriter(new FileWriter(name));
                    String line;
                    while (!(line = in.readLine()).equals("END")) fileOut.println(line);
                    fileOut.close();
                    System.out.println("File downloaded successfully");
                }
            } else if (choice == 4) {
                out.println("QUIT");
                System.out.println("Server: " + in.readLine());
                break;
            } else System.out.println("Invalid choice");
        }
        socket.close();
        scanner.close();
    }
}
