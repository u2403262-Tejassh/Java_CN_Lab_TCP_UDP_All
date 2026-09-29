import java.net.*;

public class Calculator_UDP_Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket=new DatagramSocket(6802); byte[] buffer=new byte[1024];
        while(true){
            DatagramPacket p=new DatagramPacket(buffer,buffer.length); socket.receive(p);
            String e=new String(p.getData(),0,p.getLength()); if(e.equalsIgnoreCase("exit")) break;
            String result=calculate(e); byte[] data=result.getBytes();
            socket.send(new DatagramPacket(data,data.length,p.getAddress(),p.getPort()));
        }
        socket.close();
    }
    static String calculate(String e){
        String[] p=e.trim().split(" "); if(p.length!=3)return"Use: number operator number";
        double a=Double.parseDouble(p[0]), b=Double.parseDouble(p[2]);
        switch(p[1]){case "+":return""+(a+b);case "-":return""+(a-b);case "*":return""+(a*b);case "/":return b==0?"Cannot divide by zero":""+(a/b);default:return"Invalid operator";}
    }
}
