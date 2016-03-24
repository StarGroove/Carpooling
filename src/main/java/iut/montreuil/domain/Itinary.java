package iut.montreuil.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Itinary.
 */
@Entity
@Table(name = "itinary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itinary")
public class Itinary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "location_start")
    private String locationStart;
    
    @Column(name = "location_end")
    private String locationEnd;
    
    @Column(name = "driver")
    private String driver;
    
    @Column(name = "car")
    private String car;
    
    @Column(name = "list_step")
    private String listStep;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationStart() {
        return locationStart;
    }
    
    public void setLocationStart(String locationStart) {
        this.locationStart = locationStart;
    }

    public String getLocationEnd() {
        return locationEnd;
    }
    
    public void setLocationEnd(String locationEnd) {
        this.locationEnd = locationEnd;
    }

    public String getDriver() {
        return driver;
    }
    
    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCar() {
        return car;
    }
    
    public void setCar(String car) {
        this.car = car;
    }

    public String getListStep() {
        return listStep;
    }
    
    public void setListStep(String listStep) {
        this.listStep = listStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Itinary itinary = (Itinary) o;
        if(itinary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, itinary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Itinary{" +
            "id=" + id +
            ", locationStart='" + locationStart + "'" +
            ", locationEnd='" + locationEnd + "'" +
            ", driver='" + driver + "'" +
            ", car='" + car + "'" +
            ", listStep='" + listStep + "'" +
            '}';
    }
}
