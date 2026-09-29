import java.io.*;
import java.net.*;

public class Calculator_TCP_Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6801);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            String expression = in.readLine();
            if (expression == null || expression.equalsIgnoreCase("exit")) break;
            out.println(calculate(expression));
        }
        socket.close(); serverSocket.close();
    }
    static String calculate(String e) {
        String[] p=e.trim().split(" ");
        if(p.length!=3) return "Use: number operator number";
        double a=Double.parseDouble(p[0]); double b=Double.parseDouble(p[2]);
        switch(p[1]){
            case "+": return ""+(a+b); case "-": return ""+(a-b); case "*": return ""+(a*b);
            case "/": return b==0?"Cannot divide by zero":""+(a/b); default:return "Invalid operator";
        }
    }
}
