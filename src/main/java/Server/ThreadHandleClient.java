package Server;

import GlobalStuff.MessageTypes;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadHandleClient implements Runnable {
    private Thread threadHandleClients;
    private final String threadName;
    private Server server; // The server socket
    private Socket socket; // the client socket
    private boolean running = true; // are we running or should we end?
    private int myClientId;
    private String myClientUsername;

    public ThreadHandleClient(String threadName, Server server, Socket s, int myClientId) {
        this.threadName = threadName;
        this.server = server;
        this.socket = s;
        this.myClientId = myClientId;

        this.myClientUsername = server.getClientUsername(myClientId);
    }

    @Override
    public void run() {
        System.out.println("Thread running " + threadName);

        getMessageFromClient(socket);

        System.out.println("Thread ended " + threadName);
    }

    private void getMessageFromClient(Socket s) {
        while(running) { // While we're running
            try {
                DataInputStream dIn = new DataInputStream(s.getInputStream()); // Create a new dataInputStream
                MessageTypes messageType = MessageTypes.values()[dIn.readByte()];

                switch (messageType) { // Check what type of message the client sends us
                    case DISCONNECT: // the client is disconnecting
                        boolean removedClient = false;
                        while(!removedClient) {
                            if (server.removeClientFromMap(myClientId)) { // get the ID of the client and disconnect him)
                                String disconnectMessage = myClientUsername + " disconnected";
                                server.sendToAllClientsWithText(server.getId(), MessageTypes.NORMAL_MESSAGE.getValue(), disconnectMessage);
                                running = false; // Stop this thread
                                removedClient = true;
                            }
                        }
                        break;

                    case NORMAL_MESSAGE: // Normal message
                        server.sendToAllClientsWithText(myClientId, messageType.getValue(), dIn.readUTF()); // read text from the client and send it to the server and to all the other clients. and don't send it to me back
                        break;

                    case CLIENT_COMMAND:
                        boolean isOp = dIn.readBoolean();
                        String command = dIn.readUTF();
                        int argAmount = dIn.readInt();
                        String[] args = new String[argAmount];
                        for (int i = 0; i < argAmount; i++) {
                            args[i] = dIn.readUTF();
                        }

                        if(!server.handleCommandsClient(isOp, command, args, myClientId)) {
                            server.sendToClientWithText(server.getId(), myClientId, MessageTypes.NORMAL_MESSAGE.getValue(), "Unknown command!");
                        }
                        break;

                    default:
                        String message = dIn.readUTF();
                        if(message.equals("")) {
                            message = "null";
                        }
                        System.out.println("Got undefined Message Type from client in thread " + threadName + "! Message: " + message);
                        server.sendToClientWithText(server.getId(), myClientId, messageType.getValue(),"Undefined Message Type!");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Can't get message from client in thread " + threadName);
            }
        }
    }

    public void start() {
        System.out.println("THREAD " + threadName + " STARTED");
        if (threadHandleClients == null) {
            threadHandleClients = new Thread(this, threadName);
            threadHandleClients.start();
        }
    }
}