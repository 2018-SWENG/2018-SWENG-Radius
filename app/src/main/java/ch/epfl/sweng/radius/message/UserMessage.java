package ch.epfl.sweng.radius.message;

class UserMessage extends BaseMessage {
    private User sender;

    public UserMessage(String message, long createdAt, User sender) {
        super(message, createdAt);
        this.sender = sender;
    }

    public User getSender() {
        return sender;
    }

}
