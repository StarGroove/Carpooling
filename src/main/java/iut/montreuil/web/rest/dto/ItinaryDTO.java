package iut.montreuil.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Itinary entity.
 */
public class ItinaryDTO implements Serializable {

    private Long id;

    private String locationStart;


    private String locationEnd;


    private String driver;


    private String car;


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

        ItinaryDTO itinaryDTO = (ItinaryDTO) o;

        if ( ! Objects.equals(id, itinaryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItinaryDTO{" +
            "id=" + id +
            ", locationStart='" + locationStart + "'" +
            ", locationEnd='" + locationEnd + "'" +
            ", driver='" + driver + "'" +
            ", car='" + car + "'" +
            ", listStep='" + listStep + "'" +
            '}';
    }
}
