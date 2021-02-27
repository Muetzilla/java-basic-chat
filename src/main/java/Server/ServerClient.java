package Server;

import java.net.Socket;

// This is a reference of a client for the server, for every connected client we add one instance of this class to the hashmap
public class ServerClient {
    private int myId;
    private String username = "Not Defined";
    private Socket s;
    private boolean operator = false;

    public ServerClient(int myId, String username, Socket s) {
        this.myId = myId;
        this.username = username;
        this.s = s;
    }

    public int getMyId() {
        return myId; // Get the ID of this instance
        //(Right now unused)
    }

    public String getUsername() {
        return username; // Get the username of this instance
    }

    public void setUsername(String username) {
        this.username = username; // Set the username of this client
        //(Right now unused, would be used if a method for changing names in runtime would be introduced)
    }

    public String getUsernameWithID() {
        return username + " (" + myId + ")";
    }

    public Socket getSocket() {
        return s;
    }

    public boolean isOperator() {
        return operator;
    }

    public void toggleOperator() {
        this.operator = !this.operator;
    }
}