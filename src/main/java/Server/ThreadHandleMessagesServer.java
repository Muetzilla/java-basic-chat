package Server;

import GlobalStuff.MessageTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ThreadHandleMessagesServer extends JFrame implements Runnable, ActionListener, WindowListener {
    private Thread threadHandleMessagesServer;
    private final String threadName;
    private Server server;
    private JTextField textInput;

    public ThreadHandleMessagesServer(String threadName, Server server) {
        this.threadName = threadName;
        this.server = server;
    }

    private void initInputWindow() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // if the window closes, we end the whole program
        setTitle("Server " + threadName); // Set the title of the window

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        textInput = new JTextField(15); // Create new text field
        northPanel.add(textInput);
        add(northPanel, BorderLayout.NORTH);

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(this);
        JPanel centerPanel = new JPanel();
        centerPanel.add(sendMessageButton);

        add(centerPanel, BorderLayout.CENTER);

        addWindowListener(this); // Add a window listener with which we check if the window got closed or not

        setSize(300,200); // Set the size of the window
        setVisible(true); // make it visible
    }

    @Override
    public void run() {
        System.out.println("Thread running " + threadName);

        initInputWindow();
    }

    public void start() {
        System.out.println("THREAD " + threadName + " STARTED");
        if (threadHandleMessagesServer == null) {
            threadHandleMessagesServer = new Thread(this, threadName);
            threadHandleMessagesServer.start();
        }
    }

    // If we press the button
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = textInput.getText();
        if(text.startsWith("/")) {
            text = text.toLowerCase();
            text = text.substring(1);
            String[] commandParts = text.split(" ");
            String command = commandParts[0];

            String[] args = new String[commandParts.length - 1];
            // Copy the elements of the commandParts array from index 1 into args from index 0
            if(args.length > 0) {
                System.arraycopy(commandParts, 1, args, 0, commandParts.length - 1);
            }

            if(!server.handleCommandsServer(command, args)) {
                System.out.println("Unknown command!");
            }
        } else {
            server.sendToAllClientsWithText(server.getId(), MessageTypes.NORMAL_MESSAGE.getValue(),text); // Get the text inside of the input field and send it to all the connected clients
        }
        textInput.setText(""); // Reset the input field
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    // Check if the window is closing
    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Closing " + getTitle()); // Tell the user that the window is closing
        server.sendToAllClientsNoText(MessageTypes.DISCONNECT.getValue()); // the server is disconnecting, so we tell all the connected clients that they need to disconnect
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}