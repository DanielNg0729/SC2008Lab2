import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Rfc865udpclient {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 17; // Change to 17 later
        String message = "Nguyen Hoang Duong, SCSA, CLientIPAddr";

        try {
            // get the IP address of the server from its name
            InetAddress serverAddress = InetAddress.getByName(serverHost);

            //
            // 1. Open UDP socket
            //
            DatagramSocket socket = new DatagramSocket();
            // If reply get lost, give up after 5 second
            socket.setSoTimeout(5000);
            System.out.println("Server  : " + serverAddress.getHostAddress() + " port " + serverPort);
            System.out.println("My IP   : " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("My port : " + socket.getLocalPort());
            System.out.println("Sending : " + message);

            //
            // 2. Send UDP request to server
            // 
            byte[] outBuffer = message.getBytes();
            DatagramPacket request = new DatagramPacket(outBuffer, outBuffer.length,serverAddress,serverPort);
            socket.send(request);

            //
            // 3. Receive UDP reply from server
            //
            byte[] inBuffer = new byte[512];
            DatagramPacket reply = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(reply);
            
            String quote = new String(reply.getData(), 0, reply.getLength());
 
            System.out.println("Reply from " + reply.getAddress().getHostAddress() + " port " + reply.getPort());
            System.out.println("Quote of the day: " + quote);
 
            socket.close();
        } catch (SocketTimeoutException e) {
            System.out.println("No reply after 5 seconds.");
            System.out.println("Check that the server is running and the port number is correct.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
        
    }
}
