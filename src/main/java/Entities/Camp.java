package Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCamp;
    private String site;
    private Date fromDate = new Date();
    private Date toDate = new Date();

    public Camp() {
        // Constructor predeterminado
    }

    public Camp(String site, Date fromDate, Date toDate) {
        this.site = site;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Camp(int idCamp, String site, java.sql.Date fromDate, java.sql.Date toDate) {
        this.idCamp = idCamp;
        this.site = site;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    // Getters y setters

    public int getIdCamp() {
        return idCamp;
    }

    public void setIdCamp(int idCamp) {
        this.idCamp = idCamp;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        if (site != null && !site.trim().isEmpty()) {
            this.site = site;
        } else {
            throw new IllegalArgumentException("El sitio del campamento no puede estar vacío");
        }
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "Camp [idCamp=" + idCamp + ", site=" + site + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }
}
