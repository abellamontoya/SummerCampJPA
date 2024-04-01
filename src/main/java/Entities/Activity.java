package Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Activity {

    @Id
    private int idActivity;

    private String description;

    public Activity() {
        // Constructor vacío necesario para JPA
    }

    public Activity(int idActivity, String description) {
        this.idActivity = idActivity;
        this.description = description;
    }

    // Getters y setters

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Activity [idActivity=" + idActivity + ", description=" + description + "]";
    }
}
