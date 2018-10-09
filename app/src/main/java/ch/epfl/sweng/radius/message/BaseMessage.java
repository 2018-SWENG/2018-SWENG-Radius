package ch.epfl.sweng.radius.message;

class BaseMessage {
    private String message;
    private long createdAt;

    public BaseMessage(String message, long createdAt){
        this.message=message;
        this.createdAt=createdAt;
    }
    public String getMessage() {
        return message;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
