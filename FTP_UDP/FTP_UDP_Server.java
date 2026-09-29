import java.io.*;
import java.net.*;

public class FTP_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(6402);
        new File("server").mkdirs();
        byte[] buffer = new byte[4096];
        System.out.println("FTP UDP Server waiting...");

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String command = new String(packet.getData(), 0, packet.getLength());
            if (command.equals("QUIT")) break;
            InetAddress addr = packet.getAddress(); int port = packet.getPort();
            System.out.println("Client: " + command);

            if (command.equals("LIST")) {
                StringBuilder b = new StringBuilder();
                File[] files = new File("server").listFiles();
                if (files != null) for (File f : files) b.append(f.getName()).append("\n");
                send(socket, addr, port, b.toString().isEmpty()?"No files":b.toString());
            } else if (command.startsWith("UPLOAD ")) {
                String name = command.substring(7);
                send(socket, addr, port, "READY");
                socket.receive(packet);
                byte[] fileData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, fileData, 0, packet.getLength());
                FileOutputStream out = new FileOutputStream(new File("server", name));
                out.write(fileData); out.close();
                send(socket, addr, port, "UPLOAD OK");
            } else if (command.startsWith("DOWNLOAD ")) {
                String name = command.substring(9);
                File file = new File("server", name);
                if (!file.exists()) send(socket, addr, port, "404 File not found");
                else {
                    send(socket, addr, port, "READY");
                    byte[] fileData = readBytes(file);
                    DatagramPacket dataPacket = new DatagramPacket(fileData, fileData.length, addr, port);
                    socket.send(dataPacket);
                    System.out.println("File sent");
                }
            } else send(socket, addr, port, "500 Invalid command");
        }
        socket.close();
    }

    static void send(DatagramSocket socket, InetAddress addr, int port, String text) throws Exception {
        byte[] data = text.getBytes();
        socket.send(new DatagramPacket(data, data.length, addr, port));
    }
    static byte[] readBytes(File file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        in.read(data); in.close(); return data;
    }
}
