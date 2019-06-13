package org.soptorshi.service.mapper;

import org.soptorshi.domain.*;
import org.soptorshi.service.dto.ManagerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Manager and its DTO ManagerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManagerMapper extends EntityMapper<ManagerDTO, Manager> {



    default Manager fromId(Long id) {
        if (id == null) {
            return null;
        }
        Manager manager = new Manager();
        manager.setId(id);
        return manager;
    }
}
