import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
 
public class Rfc865TcpClient {
 
    public static void main(String[] args) {
 
        String serverHost = "localhost";
        int serverPort = 1718;             // 17 in the lab
        String message = "YourName, YourLabGroup, YourClientIPAddress";
 
        try {
            //
            // 1. Establish TCP connection with server
            //
            Socket socket = new Socket(serverHost, serverPort);
            socket.setSoTimeout(5000);
 
            System.out.println("Connected to " + socket.getInetAddress().getHostAddress()
                    + " port " + socket.getPort());
            System.out.println("My IP   : " + socket.getLocalAddress().getHostAddress());
            System.out.println("My port : " + socket.getLocalPort());
 
            //
            // 2. Send TCP request to server
            //
            OutputStream out = socket.getOutputStream();
            out.write(message.getBytes());
            out.flush();
 
            //
            // 3. Receive TCP reply from server
            //    
            //
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[512];
            String quote = "";
 
            int count = in.read(buffer);
            while (count != -1) {
                quote = quote + new String(buffer, 0, count);
                count = in.read(buffer);
            }
 
            System.out.println("Quote of the day: " + quote.trim());
 
            socket.close();
 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
 