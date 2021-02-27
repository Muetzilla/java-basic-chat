package ChatCommands;

import GlobalStuff.MessageTypes;

public class ListAllConnectedClientsCommandHandler extends AbstractCommand {
    public ListAllConnectedClientsCommandHandler() {
    }

    @Override
    public void clientExecute(boolean isOp, String[] args, int senderId) {
        String message = "\nListing all connected Clients...\n";
        if(server.clientMap.size() > 0) {
            for (int i : server.clientMap.keySet()) {
                String usernameAndId = server.clientMap.get(i).getUsernameWithID();
                if(senderId == server.clientMap.get(i).getMyId()) {
                    usernameAndId += " (me)";
                }

                if(server.clientMap.get(i).isOperator()) {
                    usernameAndId += " (OP)";
                }

                message+="- " + usernameAndId + "\n";
            }
        } else {
            message += "No clients connected!";
        }

        server.sendToClientWithText(-1, senderId, MessageTypes.NORMAL_MESSAGE.getValue(), message);
    }

    @Override
    public void serverExecute(String[] args) {
        String message = "\nListing all connected Clients...\n";
        if(server.clientMap.size() > 0) {
            for (int i : server.clientMap.keySet()) {
                String usernameAndId = server.clientMap.get(i).getUsernameWithID();

                if(server.clientMap.get(i).isOperator()) {
                    usernameAndId += " (OP)";
                }

                message+="- " + usernameAndId + "\n";
            }
        } else {
            message += "No clients connected!";
        }

        System.out.println(message);
    }

    @Override
    public void printDescription() {

    }
}
