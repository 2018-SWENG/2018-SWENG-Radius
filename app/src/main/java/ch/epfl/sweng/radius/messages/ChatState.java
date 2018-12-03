package ch.epfl.sweng.radius.messages;

public class ChatState{
    private boolean isRunning;
    private int     unreadMsg;

    public ChatState(){
        this.isRunning = true;
        this.unreadMsg = 0;
    }

    public void clear(){
        isRunning = true;
        unreadMsg = 0;
    }

    public void leaveActivity(){
        isRunning = false;
    }

    public void msgReceived(){
        unreadMsg++;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public int getUnreadMsg() {
        return unreadMsg;
    }
}
