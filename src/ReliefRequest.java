package src;

public class ReliefRequest {
    private int id;
    private String requesterName;
    private String location;
    private String emergencyType;
    private String priority;
    private String status;

    public ReliefRequest(int id, String requesterName,
                         String location, String emergencyType,
                         String priority, String status) {
        this.id = id;
        this.requesterName = requesterName;
        this.location = location;
        this.emergencyType = emergencyType;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public String getLocation() {
        return location;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return id + " | " + requesterName + " | " + location
                + " | " + emergencyType + " | "
                + priority + " | " + status;
    }
}