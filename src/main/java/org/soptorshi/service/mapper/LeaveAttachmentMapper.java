package org.soptorshi.service.mapper;

import org.soptorshi.domain.*;
import org.soptorshi.service.dto.LeaveAttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LeaveAttachment and its DTO LeaveAttachmentDTO.
 */
@Mapper(componentModel = "spring", uses = {LeaveApplicationMapper.class})
public interface LeaveAttachmentMapper extends EntityMapper<LeaveAttachmentDTO, LeaveAttachment> {


    @Mapping(source = "leaveApplication.id", target = "leaveApplicationId")
    LeaveAttachmentDTO toDto(LeaveAttachment leaveAttachment);

    @Mapping(source = "leaveApplicationId", target = "leaveApplication")
    LeaveAttachment toEntity(LeaveAttachmentDTO leaveAttachmentDTO);

    default LeaveAttachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveAttachment leaveAttachment = new LeaveAttachment();
        leaveAttachment.setId(id);
        return leaveAttachment;
    }
}
