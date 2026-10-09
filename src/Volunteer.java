package src;

public class Volunteer extends Person {
    private String skill;
    private String availability;

    public Volunteer(int id, String name, String contact,
                     String skill, String availability) {
        super(id, name, contact);
        this.skill = skill;
        this.availability = availability;
    }

    public String getSkill() {
        return skill;
    }

    public String getAvailability() {
        return availability;
    }

    @Override
    public String getRole() {
        return "Volunteer";
    }

    @Override
    public String toString() {
        return getId() + " | " + getName() + " | "
                + getContact() + " | " + skill + " | "
                + availability;
    }
}
