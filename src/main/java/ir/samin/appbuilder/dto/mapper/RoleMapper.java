package ir.samin.appbuilder.dto.mapper;

import ir.samin.appbuilder.dto.PageDTO;
import ir.samin.appbuilder.dto.RoleDTO;
import ir.samin.appbuilder.entity.Role;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public abstract class RoleMapper {

    public abstract List<RoleDTO> mapToListOfDto(List<Role> rolesDTOS);

    public PageDTO<RoleDTO> toPageDTO(Page<Role> rolesDTOS) {
        if (rolesDTOS == null) {
            return null;
        }
        PageDTO<RoleDTO> pageDTO = new PageDTO<>();
        List<RoleDTO> notificationFullDTOS = mapToListOfDto(rolesDTOS.getContent());
        pageDTO.setContent(notificationFullDTOS);
        pageDTO.setNumber(rolesDTOS.getNumber());
        pageDTO.setTotalElements(rolesDTOS.getTotalElements());
        pageDTO.setTotalPages(rolesDTOS.getTotalPages());
        pageDTO.setSize(rolesDTOS.getSize());
        pageDTO.setMessage("Roles Returned Successfully");
        return pageDTO;
    }

    public abstract Role toEntity(RoleDTO roleDTO);

    public abstract RoleDTO toDTO(Role role);
}
