package ChatCommands;

import Server.Server;

public abstract class AbstractCommand {
    public boolean serverOnly = false;
    public boolean opOnly = false;

    protected Server server = Server.get();
    protected int[] targetList;

    protected String youNeedOp = "You need to have OP rights to use this command!";
    protected String needTargetId = "Pls tell me who is the target of this command (ID of the client)";
    protected String thisUserDoesNotExist = "This user does not exist! use the command \"/lc\" to see the connected users!";
    protected String iNeedANumber = "Pls enter a number as an argument!";

    protected boolean getTargetId(String[] idList) {
        targetList = new int[idList.length];

        for (int i = 0; i < targetList.length; i++) {
            try {
                targetList[i] = Integer.parseInt(idList[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean isServerOnly() {
        return serverOnly;
    }

    public boolean isOpOnly() {
        return opOnly;
    }

    public abstract void clientExecute(boolean isOp, String[] args, int senderId);

    public abstract void serverExecute(String[] args);
    // TODO: actually implement this method
    public abstract void printDescription(); // If using this command with "help" as an argument, call this method
}
