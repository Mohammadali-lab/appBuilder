package ir.samin.appbuilder.dto.mapper;

import ir.samin.appbuilder.dto.AdminCreationDTO;
import ir.samin.appbuilder.dto.AdminFullDTO;
import ir.samin.appbuilder.dto.PageDTO;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Named("setRoleNull")
    Admin toEntity(AdminCreationDTO userAuthRequestDTO);

    @Mapping(source = "role", target = "role", qualifiedByName = "setRoleNull")
    public static Role setRoleNull(String role){
        return null;
    }

    AdminFullDTO toFullDTO(Admin admin);

    List<AdminFullDTO> mapToListOfDto(List<Admin> content);


    default PageDTO<AdminFullDTO> ToPageDTO(Page<Admin> admins){
        if (admins == null) {
            return null;
        }
        PageDTO<AdminFullDTO> pageDTO = new PageDTO<>();
        List<AdminFullDTO> AdminFullDTOS = mapToListOfDto(admins.getContent());
        pageDTO.setContent(AdminFullDTOS);
        pageDTO.setNumber(admins.getNumber());
        pageDTO.setTotalElements(admins.getTotalElements());
        pageDTO.setTotalPages(admins.getTotalPages());
        pageDTO.setSize(admins.getSize());
        pageDTO.setMessage("Admins Returned Successfully");
        return pageDTO;
    }

    Admin toEntity(AdminFullDTO adminFullDTO);
}
