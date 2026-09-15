import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
 
public class Rfc865TcpServer {
 
    public static void main(String[] args) {
 
        int port = 17;                   // 17 in lab
 
        String[] quotes = {
            "The unexamined life is not worth living. -- Socrates",
            "Well begun is half done. -- Aristotle",
            "Real knowledge is to know the extent of one's ignorance. -- Confucius",
            "An investment in knowledge pays the best interest. -- Benjamin Franklin",
            "A journey of a thousand miles begins with a single step. -- Lao Tzu"
        };
 
        Random random = new Random();
        ServerSocket parentSocket = null;
 
        //
        // 1. Open TCP socket at well-known port
        //
        try {
            parentSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Cannot open TCP port " + port + ": " + e.getMessage());
            return;
        }
 
        System.out.println("RFC 865 TCP server is listening on port " + port);
        System.out.println("Press Ctrl-C to stop.");
 
        while (true) {
            try {
                //
                // 2. Listen to establish TCP connection with client
                //
                Socket childSocket = parentSocket.accept();   // waits here
 
                // Pick the quote here and pass it to the thread.
                String quote = quotes[random.nextInt(quotes.length)];
 
                //
                // 3. Create new thread to handle client connection
                //
                ClientHandler client = new ClientHandler(childSocket, quote);
                Thread thread = new Thread(client);
                thread.start();
 
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
 
class ClientHandler implements Runnable {
 
    private Socket socket;
    private String quote;
 
    ClientHandler(Socket socket, String quote) {
        this.socket = socket;
        this.quote = quote;
    }
 
    public void run() {
        try {
            System.out.println("Connection from " + socket.getInetAddress().getHostAddress()
                    + " port " + socket.getPort());
 
            //
            // 4. Receive TCP request from client
            //    
            //    
            socket.setSoTimeout(2000);
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[512];
 
            try {
                int count = in.read(buffer);
                if (count > 0) {
                    System.out.println("Request: " + new String(buffer, 0, count));
                }
            } catch (Exception e) {
                System.out.println("No request data received.");
            }
 
            //
            // 5. Send TCP reply to client
            //
            String reply = quote;
            if (reply.length() > 512) {
                reply = reply.substring(0, 512);
            }
 
            OutputStream out = socket.getOutputStream();
            out.write((reply + "\r\n").getBytes());
            out.flush();
 
            System.out.println("Sent quote: " + reply);
 
            socket.close();
 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
 