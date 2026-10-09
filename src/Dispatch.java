package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    
public static void viewDispatches(Connection con)
        throws SQLException {

    String sql = """
        SELECT d.dispatch_id, d.request_id,
               r.location, v.full_name, d.status
        FROM dispatches d
        JOIN relief_requests r
            ON d.request_id = r.request_id
        JOIN volunteers v
            ON d.volunteer_id = v.volunteer_id
        ORDER BY d.dispatch_id
        """;

    try (Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        System.out.println(
            "Dispatch ID | Request ID | Location | Volunteer | Status"
        );

        while (rs.next()) {
            System.out.println(
                rs.getInt("dispatch_id") + " | " +
                rs.getInt("request_id") + " | " +
                rs.getString("location") + " | " +
                rs.getString("full_name") + " | " +
                rs.getString("status")
            );
        }
    }
}


public static int addDispatch(
        Connection con, int requestId, int volunteerId)
        throws SQLException {

   
            
    String sql = """
        INSERT INTO dispatches
            (request_id, volunteer_id, status)
        VALUES (?, ?, ?)
        """;

    try (PreparedStatement ps = con.prepareStatement(
            sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setInt(1, requestId);
        ps.setInt(2, volunteerId);
        ps.setString(3, "Planned");

        ps.executeUpdate();

        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) {
                int dispatchId = keys.getInt(1);
                System.out.println("Dispatch created successfully. ID: "
                        + dispatchId);
                return dispatchId;
            }
        }
    }

    throw new SQLException("Dispatch was created but its ID could not be retrieved.");
        }
    }
    
        
