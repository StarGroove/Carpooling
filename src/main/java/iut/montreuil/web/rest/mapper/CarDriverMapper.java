package iut.montreuil.web.rest.mapper;

import iut.montreuil.domain.*;
import iut.montreuil.web.rest.dto.CarDriverDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CarDriver and its DTO CarDriverDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarDriverMapper {

    CarDriverDTO carDriverToCarDriverDTO(CarDriver carDriver);

    CarDriver carDriverDTOToCarDriver(CarDriverDTO carDriverDTO);
}
