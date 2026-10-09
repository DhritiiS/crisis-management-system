package src;

import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("======================================");
            System.out.println(" CRISIS MANAGEMENT AND RELIEF SYSTEM");
            System.out.println("======================================");

            int choice;

            do {
                System.out.println("\n1. View Volunteers");
                System.out.println("2. View Inventory");
                System.out.println("3. View Relief Requests");
                System.out.println("4. View Request Summary");
                System.out.println("5. View Dispatches");
                System.out.println("6. Register Relief Request");
                System.out.println("7. Add Volunteer");
                System.out.println("8. Update Inventory Stock");
                System.out.println("9. Update Relief Request Status");
                System.out.println("10. Allocate Relief Supplies");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");

                while (!sc.hasNextInt()) {
                    System.out.print("Enter a valid number: ");
                    sc.next();
                }

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> viewVolunteers(con);
                    case 2 -> viewInventory(con);
                    case 3 -> viewRequests(con);
                    case 4 -> viewSummary(con);
                    case 5 -> viewDispatches(con);
                    case 6 -> addRequest(con);
                    case 7 -> addVolunteer(con);
                    case 8 -> updateInventoryStock(con);
                    case 9 -> updateRequestStatus(con);
                    case 10 -> allocateSupplies(con);
                    case 0 -> System.out.println("Exiting application...");
                    default -> System.out.println("Invalid choice.");
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    private static void viewVolunteers(Connection con)
            throws SQLException {
        String sql = "SELECT volunteer_id, full_name, phone, skill, availability "
                   + "FROM volunteers";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nID | Name | Phone | Skill | Availability");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("volunteer_id") + " | "
                    + rs.getString("full_name") + " | "
                    + rs.getString("phone") + " | "
                    + rs.getString("skill") + " | "
                    + rs.getString("availability")
                );
            }
        }
    }

    private static void viewInventory(Connection con)
            throws SQLException {
        String sql = "SELECT item_id, item_name, category, "
                   + "quantity_available, unit FROM inventory_items";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nID | Item | Category | Quantity | Unit");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("item_id") + " | "
                    + rs.getString("item_name") + " | "
                    + rs.getString("category") + " | "
                    + rs.getInt("quantity_available") + " | "
                    + rs.getString("unit")
                );
            }
        }
    }

    private static void viewRequests(Connection con)
            throws SQLException {
        String sql = "SELECT request_id, requester_name, location, "
                   + "emergency_type, priority, status FROM relief_requests";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nID | Requester | Location | Emergency | Priority | Status");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("request_id") + " | "
                    + rs.getString("requester_name") + " | "
                    + rs.getString("location") + " | "
                    + rs.getString("emergency_type") + " | "
                    + rs.getString("priority") + " | "
                    + rs.getString("status")
                );
            }
        }
    }

    private static void viewSummary(Connection con)
            throws SQLException {
        String sql = "SELECT * FROM vw_request_summary";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nID | Requester | Location | Emergency | Priority | Status | Items");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("request_id") + " | "
                    + rs.getString("requester_name") + " | "
                    + rs.getString("location") + " | "
                    + rs.getString("emergency_type") + " | "
                    + rs.getString("priority") + " | "
                    + rs.getString("status") + " | "
                    + rs.getInt("different_items_requested")
                );
            }
        }
    }

    private static void viewDispatches(Connection con)
            throws SQLException {
        String sql = "SELECT d.dispatch_id, r.location, v.full_name, "
                   + "d.status, d.created_at "
                   + "FROM dispatches d "
                   + "JOIN relief_requests r ON d.request_id = r.request_id "
                   + "JOIN volunteers v ON d.volunteer_id = v.volunteer_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nDispatch ID | Location | Volunteer | Status | Created");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("dispatch_id") + " | "
                    + rs.getString("location") + " | "
                    + rs.getString("full_name") + " | "
                    + rs.getString("status") + " | "
                    + rs.getTimestamp("created_at")
                );
            }
        }
    }
    
private static void addRequest(Connection con) throws SQLException {
    System.out.println("\n--- Register New Relief Request ---");

    System.out.print("Enter requester name: ");
    String name = sc.nextLine().trim();

    System.out.print("Enter contact number: ");
    String contact = sc.nextLine().trim();

    System.out.print("Enter location: ");
    String location = sc.nextLine().trim();

    System.out.println("Emergency types: Flood, Earthquake, Fire, Cyclone, Landslide, Other");
    System.out.print("Enter emergency type: ");
    String emergency = sc.nextLine().trim();

    System.out.println("Priorities: Low, Medium, High, Critical");
    System.out.print("Enter priority: ");
    String priority = sc.nextLine().trim();

    System.out.print("Enter description: ");
    String description = sc.nextLine().trim();

    if (name.isEmpty() || location.isEmpty()) {
        System.out.println("Requester name and location are required.");
        return;
    }

    String sql = "INSERT INTO relief_requests "
            + "(requester_name, requester_contact, location, "
            + "emergency_type, description, priority) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setString(2, contact.isEmpty() ? null : contact);
        ps.setString(3, location);
        ps.setString(4, emergency);
        ps.setString(5, description);
        ps.setString(6, priority);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Relief request registered successfully!");
        }
    } catch (SQLException e) {
        System.out.println("Could not register request. Check the emergency type and priority.");
        System.out.println("Details: " + e.getMessage());
    }
}

private static void addVolunteer(Connection con) throws SQLException {
    System.out.println("\n--- Add New Volunteer ---");

    System.out.print("Enter volunteer name: ");
    String name = sc.nextLine().trim();

    System.out.print("Enter phone number: ");
    String phone = sc.nextLine().trim();

    System.out.print("Enter email (optional): ");
    String email = sc.nextLine().trim();

    System.out.print("Enter skill (e.g., First Aid, Logistics): ");
    String skill = sc.nextLine().trim();

    if (name.isEmpty() || phone.isEmpty()) {
        System.out.println("Name and phone number are required.");
        return;
    }

    String sql = "INSERT INTO volunteers "
            + "(full_name, phone, email, skill, availability) "
            + "VALUES (?, ?, ?, ?, 'Available')";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, email.isEmpty() ? null : email);
        ps.setString(4, skill.isEmpty() ? null : skill);

        ps.executeUpdate();
        System.out.println("Volunteer added successfully!");

    } catch (SQLException e) {
        System.out.println("Could not add volunteer. The phone or email may already exist.");
        System.out.println("Details: " + e.getMessage());
    }
}

private static void updateInventoryStock(Connection con)
        throws SQLException {
    System.out.println("\n--- Update Inventory Stock ---");

    viewInventory(con);

    System.out.print("\nEnter item ID to update: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid item ID.");
        sc.nextLine();
        return;
    }
    int itemId = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter quantity to add: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid quantity.");
        sc.nextLine();
        return;
    }
    int quantityToAdd = sc.nextInt();
    sc.nextLine();

    if (quantityToAdd <= 0) {
        System.out.println("Quantity must be greater than zero.");
        return;
    }

    String sql = "UPDATE inventory_items "
            + "SET quantity_available = quantity_available + ? "
            + "WHERE item_id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, quantityToAdd);
        ps.setInt(2, itemId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Inventory stock updated successfully!");
            viewInventory(con);
        } else {
            System.out.println("No inventory item found with that ID.");
        }
    }
}

private static void updateRequestStatus(Connection con)
        throws SQLException {
    System.out.println("\n--- Update Relief Request Status ---");

    viewRequests(con);

    System.out.print("\nEnter request ID: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid request ID.");
        sc.nextLine();
        return;
    }

    int requestId = sc.nextInt();
    sc.nextLine();

    System.out.println(
        "Statuses: Pending, Approved, In Progress, Fulfilled, Cancelled"
    );
    System.out.print("Enter new status: ");
    String status = sc.nextLine().trim();

    String sql = "UPDATE relief_requests SET status = ? WHERE request_id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, status);
        ps.setInt(2, requestId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Request status updated successfully!");
        } else {
            System.out.println("No request found with that ID.");
        }
    } catch (SQLException e) {
        System.out.println("Could not update status. Check the status spelling.");
        System.out.println("Details: " + e.getMessage());
    }
}

private static void allocateSupplies(Connection con)
        throws SQLException {

    System.out.println("\n--- Allocate Relief Supplies ---");

    viewRequests(con);
    viewInventory(con);

    System.out.print("\nEnter relief request ID: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid request ID.");
        sc.nextLine();
        return;
    }
    int requestId = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter inventory item ID: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid item ID.");
        sc.nextLine();
        return;
    }
    int itemId = sc.nextInt();
    sc.nextLine();

    System.out.print("Enter quantity to allocate: ");
    if (!sc.hasNextInt()) {
        System.out.println("Invalid quantity.");
        sc.nextLine();
        return;
    }
    int quantity = sc.nextInt();
    sc.nextLine();

    if (quantity <= 0) {
        System.out.println("Quantity must be greater than zero.");
        return;
    }

    
viewVolunteers(con);

System.out.print("Enter volunteer ID for this dispatch: ");

if (!sc.hasNextInt()) {
    System.out.println("Invalid volunteer ID.");
    sc.nextLine();
    return;
}

int volunteerId = sc.nextInt();
sc.nextLine();


    boolean oldAutoCommit = con.getAutoCommit();

    try {
        con.setAutoCommit(false);

        // Confirm the request exists and lock its row.
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT status FROM relief_requests WHERE request_id = ? FOR UPDATE")) {
            ps.setInt(1, requestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Relief request not found.");
                }

                if ("Fulfilled".equals(rs.getString("status"))
                        || "Cancelled".equals(rs.getString("status"))) {
                    throw new SQLException(
                        "Cannot allocate supplies to a fulfilled or cancelled request."
                    );
                }
            }
        }

        // Reduce stock only if enough quantity is available.
        String stockSql = "UPDATE inventory_items "
                + "SET quantity_available = quantity_available - ? "
                + "WHERE item_id = ? AND quantity_available >= ?";

        try (PreparedStatement ps = con.prepareStatement(stockSql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);

            if (ps.executeUpdate() == 0) {
                throw new SQLException(
                    "Item not found or insufficient stock available."
                );
            }
        }

        // Record the allocation in dispatch_items once a dispatch exists.
        // For now, store the allocated amount in request_items.
        String allocationSql =
                "INSERT INTO request_items (request_id, item_id, quantity_needed) "
              + "VALUES (?, ?, ?) "
              + "ON DUPLICATE KEY UPDATE quantity_needed = quantity_needed + ?";

        try (PreparedStatement ps = con.prepareStatement(allocationSql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, itemId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        }

        
int dispatchId = Dispatch.addDispatch(con, requestId, volunteerId);

String dispatchItemSql =
        "INSERT INTO dispatch_items (dispatch_id, item_id, quantity) "
        + "VALUES (?, ?, ?)";

try (PreparedStatement ps =
        con.prepareStatement(dispatchItemSql)) {
    ps.setInt(1, dispatchId);
    ps.setInt(2, itemId);
    ps.setInt(3, quantity);
    ps.executeUpdate();
}


        con.commit();
        System.out.println("Supplies allocated successfully!");

    } catch (SQLException e) {
        con.rollback();
        System.out.println("Allocation failed: " + e.getMessage());

    } finally {
        con.setAutoCommit(oldAutoCommit);
    }
}

}
