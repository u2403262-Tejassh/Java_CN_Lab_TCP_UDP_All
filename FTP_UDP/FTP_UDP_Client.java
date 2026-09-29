import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FTP_UDP_Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. LIST");
            System.out.println("2. UPLOAD");
            System.out.println("3. DOWNLOAD");
            System.out.println("4. QUIT");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            if (choice == 1) {
                send(socket,address,6402,"LIST");
                System.out.println(receive(socket));
            } else if (choice == 2) {
                System.out.print("Enter local file name: ");
                String name = scanner.nextLine();
                File file = new File(name);
                if (!file.exists()) { System.out.println("File not found"); continue; }
                send(socket,address,6402,"UPLOAD " + name);
                System.out.println("Server: " + receive(socket));
                byte[] data = readBytes(file);
                socket.send(new DatagramPacket(data, data.length, address, 6402));
                System.out.println("Server: " + receive(socket));
            } else if (choice == 3) {
                System.out.print("Enter file name: ");
                String name = scanner.nextLine();
                send(socket,address,6402,"DOWNLOAD " + name);
                String response = receive(socket);
                if (response.equals("404 File not found")) System.out.println(response);
                else {
                    byte[] buffer = new byte[65507];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    FileOutputStream out = new FileOutputStream(name);
                    out.write(packet.getData(), 0, packet.getLength()); out.close();
                    System.out.println("File downloaded successfully");
                }
            } else if (choice == 4) {
                send(socket,address,6402,"QUIT"); break;
            }
        }
        socket.close(); scanner.close();
    }
    static void send(DatagramSocket socket, InetAddress address, int port, String text) throws Exception {
        byte[] data = text.getBytes(); socket.send(new DatagramPacket(data, data.length, address, port));
    }
    static String receive(DatagramSocket socket) throws Exception {
        byte[] data = new byte[4096]; DatagramPacket p = new DatagramPacket(data, data.length);
        socket.receive(p); return new String(p.getData(),0,p.getLength());
    }
    static byte[] readBytes(File file) throws Exception {
        FileInputStream in = new FileInputStream(file); byte[] data = new byte[(int)file.length()];
        in.read(data); in.close(); return data;
    }
}
