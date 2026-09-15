import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

public class RFC865UDPServer {
    public static void main(String[] args) {
        // Port 17 need admin rights
        int port = 17;
        String[] quotes = {
            "The unexamined life is not worth living. -- Socrates",
            "Well begun is half done. -- Aristotle",
            "Real knowledge is to know the extent of one's ignorance. -- Confucius",
            "An investment in knowledge pays the best interest. -- Benjamin Franklin",
            "A journey of a thousand miles begins with a single step. -- Lao Tzu"
        };
        Random random = new Random();
        DatagramSocket socket;
        //
        // 1. Open UDP socket at well know port
        //
        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            System.out.println("Cannot open UDP port " + port + ": " + e.getMessage());
            return;
        }
        System.out.println("RFC 865 UDP server is listening on port " + port);
        System.out.println("Press Ctrl-C to stop.");
        
        while (true) {
            try {
                //
                // 2. Listen for UDP request from client
                //
                byte[] inBuffer = new byte[512];
                DatagramPacket request = new DatagramPacket(inBuffer, inBuffer.length);
                socket.receive(request); // the program waits here until a packet comes
                // RFC 865 lets us ignore this data. We print it, because the lab
                // server also writes down the "Name, Group, IP" that it receives.
                String text = new String(request.getData(), 0, request.getLength());
                System.out.println("Request from " + request.getAddress().getHostAddress() + " port " + request.getPort() + ": " + text);
                
                //
                // 3. Send UDP reply to client
                //
                String quote = quotes[random.nextInt(quotes.length)];
                if (quote.length() > 512) { // RFC 865 size limit
                    quote = quote.substring(0,512);
                }
                byte[] outBuffer = quote.getBytes();
                DatagramPacket reply = new DatagramPacket(outBuffer, outBuffer.length,request.getAddress(), request.getPort());
                socket.send(reply);
                System.out.println("Send quote: " + quote);
            } catch (Exception e) {
                // One bad packet must not stop the server, so we print and keep going.
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
