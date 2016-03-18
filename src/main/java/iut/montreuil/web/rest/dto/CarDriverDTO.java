package iut.montreuil.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the CarDriver entity.
 */
public class CarDriverDTO implements Serializable {

    private Long id;

    private String name;


    private String adresse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarDriverDTO carDriverDTO = (CarDriverDTO) o;

        if ( ! Objects.equals(id, carDriverDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CarDriverDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", adresse='" + adresse + "'" +
            '}';
    }
}
