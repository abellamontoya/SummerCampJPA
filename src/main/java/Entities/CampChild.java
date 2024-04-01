package Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class CampChild implements Serializable {


    @Id
    @ManyToOne
    @JoinColumn(name = "idCamp")
    private Camp camp;

    @Id
    @ManyToOne
    @JoinColumn(name = "idChild")
    private Child child;

    public CampChild() {
        // Constructor vacío
    }

    public CampChild(Camp camp, Child child) {
        this.camp = camp;
        this.child = child;
    }

    // Getters y setters

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "CampChild [camp=" + camp + ", child=" + child + "]";
    }
}
