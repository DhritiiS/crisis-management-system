package src;

public class InventoryItem {
    private int id;
    private String name;
    private String category;
    private String unit;
    private int quantity;

    public InventoryItem(int id, String name, String category,
                         String unit, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + category
                + " | " + quantity + " " + unit;
    }
}
