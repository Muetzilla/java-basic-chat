import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends ServerAndClient {
    String ipOfServer;
    private DataInputStream dIn;
    private DataOutputStream dOut;

    public static void main(String[] args) throws IOException {
        new Client();
    }

    public Client() throws IOException {
        this.ipOfServer = getString("The IP of the server").toLowerCase();
        this.port = getInt("The open Port of the server");

        this.username = getString("Your username");

        socket = new Socket(ipOfServer, port); // Create socket, connect with host on port
        dIn = new DataInputStream(socket.getInputStream());
        System.out.println("Connected with server on IP " + ipOfServer + " and Port " + port);

        myId = dIn.readInt();
        System.out.println("My ID: " + myId);

        dOut = new DataOutputStream(socket.getOutputStream());
        dOut.writeUTF(username);
        dOut.flush(); // Send off the data

        ThreadOutput threadOutput = new ThreadOutput("ThreadOutputClient", socket);
        threadOutput.start();
        ThreadInput threadInput = new ThreadInput(username, "ThreadInputClient", socket);
        threadInput.start();
    }
}