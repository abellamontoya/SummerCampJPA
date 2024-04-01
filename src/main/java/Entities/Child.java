package Entities;

import Entities.Guardian;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idChild;

    private String name;
    private Date birthday;
    private boolean specialMenu;

    @ManyToOne
    @JoinColumn(name = "guardian_dni")
    private Guardian guardian;

    // Constructor con todos los campos
    public Child(String name, Date birthday, boolean specialMenu, Guardian guardian) {
        this.name = name;
        this.birthday = birthday;
        this.specialMenu = specialMenu;
        this.guardian = guardian;
    }

    // Constructor vacío para JPA
    public Child() {
        // Constructor vacío requerido por JPA
    }
    public Child(int idChild, String name, Date birthday, boolean specialMenu, Guardian guardian) {
        this.idChild = idChild;
        this.name = name;
        this.birthday = birthday;
        this.specialMenu = specialMenu;
        this.guardian = guardian;
    }


    // Getters y setters

    public int getIdChild() {
        return idChild;
    }

    public void setIdChild(int idChild) {
        this.idChild = idChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isSpecialMenu() {
        return specialMenu;
    }

    public void setSpecialMenu(boolean specialMenu) {
        this.specialMenu = specialMenu;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    @Override
    public String toString() {
        String guardianName = (guardian != null) ? guardian.getName() : "N/A";
        return "Child [idChild=" + idChild + ", name=" + name + ", birthday=" + birthday + ", specialMenu=" + specialMenu + ", guardian=" + guardianName + "]";
    }


}
