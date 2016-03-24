package iut.montreuil.repository;

import iut.montreuil.domain.Itinary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Itinary entity.
 */
public interface ItinaryRepository extends JpaRepository<Itinary,Long> {

}
