package Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ChildActivity implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    @Id
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public ChildActivity() {
        // Constructor vacío
    }

    public ChildActivity(Child child, Activity activity) {
        this.child = child;
        this.activity = activity;
    }

    // Getters y setters

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "ChildActivity [child=" + child + ", activity=" + activity + "]";
    }
}
