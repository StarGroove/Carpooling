package iut.montreuil.web.rest.mapper;

import iut.montreuil.domain.*;
import iut.montreuil.web.rest.dto.ItinaryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Itinary and its DTO ItinaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItinaryMapper {

    ItinaryDTO itinaryToItinaryDTO(Itinary itinary);

    Itinary itinaryDTOToItinary(ItinaryDTO itinaryDTO);
}
