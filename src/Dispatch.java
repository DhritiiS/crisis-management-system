package src;

public class Dispatch {
    private int id;
    private int requestId;
    private int volunteerId;
    private String status;

    public Dispatch(int id, int requestId,
                    int volunteerId, String status) {
        this.id = id;
        this.requestId = requestId;
        this.volunteerId = volunteerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return id + " | Request: " + requestId
                + " | Volunteer: " + volunteerId
                + " | " + status;
    }
}