package ChatCommands;

import GlobalStuff.MessageTypes;
import Server.ServerClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class KickClientCommandHandler extends AbstractCommand {
    private String reasonForKickMain = "";
    private final String reasonForKickStartToKickedClient = "You've been kicked\n" +
            "Reason: ";
    private final String getReasonForKickStartToAllOtherClients = " has been kicked\n" +
            "Reason: " ;

    public KickClientCommandHandler() {
        opOnly = true;
    }

    @Override
    public void clientExecute(boolean isOp, String[] args, int senderId) {
        kick(args, senderId);
    }

    @Override
    public void serverExecute(String[] args) {
        kick(args, server.getId());
    }

    private void kick(String[] args, int senderId) {
        if(senderId == server.getId()) {
            System.out.println("Kicking client...");
        } else {
            server.sendToClientWithText(server.getId(), senderId, MessageTypes.NORMAL_MESSAGE.getValue(), "Kicking client...");
        }

        int argsLength = args.length;

        if (argsLength >= 1) {
            if(argsLength >= 2) {
                for(int i = 1; i < args.length; i++) {
                    String reasonPart = args[i];
                    reasonForKickMain += reasonPart + " ";
                }
            } else {
                reasonForKickMain = getReasonForKickMain();
            }
        } else {
            if(senderId > 0) {
                server.sendToClientWithText(server.getId(), senderId, MessageTypes.NORMAL_MESSAGE.getValue(), needTargetId);
            } else {
                System.out.println(needTargetId);
            }
        }

        if(args[0].equalsIgnoreCase("all")) {
            server.sendToAllClientsWithText(server.getId(), MessageTypes.KICK_CLIENT.getValue(), reasonForKickStartToKickedClient + reasonForKickMain);
        } else {
            String[] multipleTargetsString = args[0].split(",");
            if (getTargetId(multipleTargetsString)) {
                for (int i : targetList) {
                    if (server.checkIfClientExists(i)) {
                        ServerClient clientTarget = server.clientMap.get(i);
                        String clientTargetUsername = clientTarget.getUsernameWithID();
                        server.sendToClientWithText(senderId, i, MessageTypes.KICK_CLIENT.getValue(), reasonForKickStartToKickedClient + reasonForKickMain); // Kick the client
                        server.sendToAllClientsWithText(senderId, MessageTypes.NORMAL_MESSAGE.getValue(), clientTargetUsername + getReasonForKickStartToAllOtherClients + reasonForKickMain); // Tell all the other clients who got kicked
                    } else {
                        if(senderId > 0) {
                            server.sendToClientWithText(server.getId(), senderId, MessageTypes.NORMAL_MESSAGE.getValue(), "User with ID " + i + " does not exist!");
                        } else {
                            System.out.println("User with ID " + i + " does not exist!");
                        }
                    }
                }
            } else {
                server.sendToClientWithText(server.getId(), senderId, MessageTypes.NORMAL_MESSAGE.getValue(), iNeedANumber);
            }
        }
    }

    private String getReasonForKickMain() {
        BufferedReader reader = null;
        int amountOfLines = 0;
        String filename = "kickReasons.txt";
        File kickReasons = new File(filename);
        if(kickReasons.exists()) {
            try {
                reader = new BufferedReader(new FileReader(kickReasons));

                while (true) {
                    try {
                        if (reader.readLine() == null) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    amountOfLines++;
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Random rand = new Random();
            int n = rand.nextInt(amountOfLines);

            try (Stream<String> all_lines = Files.lines(Paths.get(filename))) {
                return all_lines.skip(n).findFirst().get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "KickReasons.txt not found!";
        }

        return "";
    }

    @Override
    public void printDescription() {

    }
}
