import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Calculator_TCP_Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost",6801);
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
        Scanner scanner=new Scanner(System.in);
        while(true){
            System.out.print("Enter expression (example: 5 + 3): ");
            String e=scanner.nextLine(); out.println(e);
            if(e.equalsIgnoreCase("exit")) break;
            System.out.println("Result: "+in.readLine());
        }
        socket.close(); scanner.close();
    }
}
