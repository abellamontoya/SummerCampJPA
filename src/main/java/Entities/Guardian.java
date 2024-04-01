package Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Guardian {

    @Id
    private String dni;
    private String name;
    private String phone;
    private String address;
    private String email;

    public Guardian() {
        // Constructor vacío
    }

    public Guardian(String dni, String name, String phone, String address, String email) {
        this.dni = dni;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    // Getters y setters

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Guardian [dni=" + dni + ", name=" + name + ", phone=" + phone + ", address=" + address + ", email=" + email
                + "]";
    }
}
