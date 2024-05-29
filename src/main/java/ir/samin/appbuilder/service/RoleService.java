package ir.samin.appbuilder.service;


import ir.samin.appbuilder.dao.PrivilegeRepository;
import ir.samin.appbuilder.dao.PrivilegeRoleRepository;
import ir.samin.appbuilder.dao.RoleRepository;
import ir.samin.appbuilder.dto.PageDTO;
import ir.samin.appbuilder.dto.RoleDTO;
import ir.samin.appbuilder.dto.mapper.RoleMapper;
import ir.samin.appbuilder.entity.*;
import ir.samin.appbuilder.exception.NotFoundException;
import ir.samin.appbuilder.exception.PaginationException;
import ir.samin.appbuilder.specification.GeneralSpecificationBuilder;
import ir.samin.appbuilder.specification.GeneralSpecificationList;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class RoleService {


    private PrivilegeRepository privilegeRepository;
    private PrivilegeRoleRepository privilegeRoleRepository;

    private final GeneralSpecificationList<Role> roleSpecificationList;
//    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository, PrivilegeRoleRepository privilegeRoleRepository
    , PrivilegeRepository privilegeRepository, GeneralSpecificationList<Role> roleSpecificationList) {
        this.roleRepository = roleRepository;
        this.privilegeRoleRepository = privilegeRoleRepository;
        this.privilegeRepository = privilegeRepository;
        this.roleSpecificationList = roleSpecificationList;
    }

    public Role findByEnglishName(String name) {
        return roleRepository.findByEnglishName(name);
    }


//    public PageDTO<RoleDTO> getRoles(Integer page, Integer size, String englishName, String persianName, LocalDateTime createdAtDateTimeFrom, LocalDateTime createdAtDateTimeTo, RoleOrderBy orderBy, Sorting sorting, Boolean isActive) {
//        if (page < 1 || size < 1) {
//            throw new PaginationException("Invalid Size Or Page");
//        }
//        Pageable pageable;
//        GeneralSpecificationBuilder<Role> builder = new GeneralSpecificationBuilder<>();
//        if (englishName != null) {
//            builder.with("englishName", ":", englishName, false);
//        }
//        if (persianName != null) {
//            builder.with("persianName", ":", persianName, false);
//        }
//        if (isActive != null) {
//            builder.with("isActive", "=", isActive, false);
//        }
//        if (orderBy != null) {
//            if (sorting == Sorting.ASC) {
//                pageable = PageRequest.of(--page, size, Sort.by(orderBy.getName()).ascending());
//            } else if (sorting == Sorting.DESC) {
//                pageable = PageRequest.of(--page, size, Sort.by(orderBy.getName()).descending());
//            } else {
//                throw new NotFoundException("Invalid Sort");
//            }
//        } else {
//            pageable = PageRequest.of(--page, size);
//        }
//        Specification<Role> build = builder.build();
//
//        build = build != null ? build.and(roleSpecificationList.dateSpecification(createdAtDateTimeFrom, createdAtDateTimeTo, "createdAtDateTime")) :
//                roleSpecificationList.dateSpecification(createdAtDateTimeFrom, createdAtDateTimeTo, "createdAtDateTime");
//
//        Page<Role> all = roleRepository.findAll(build, pageable);
//        PageDTO<RoleDTO> roleDTOPageDTO = roleMapper.toPageDTO(all);
//        for (int i = 0; i < roleDTOPageDTO.getContent().size(); i++) {
//            Set<PrivilegeRole> privilegeRoles = all.getContent().get(i).getPrivilegeRoles();
//            int finalI = i;
//            privilegeRoles.forEach(c -> {
//                roleDTOPageDTO.getContent().get(finalI).addPrivileges(c.getPrivilege().getPrivilege());
//            });
//        }
//        return roleDTOPageDTO;
//    }

//    public RoleDTO create(RoleDTO roleDTO) {
//        Role role = addPrivilegeRole(roleDTO);
//        return roleMapper.toDTO(role);
//    }

//    public RoleDTO getRoleDetails(long id) {
//        Role role = roleRepository.findById(id).get();
//        RoleDTO roleDTO = roleMapper.toDTO(role);
//        Set<PrivilegeRole> privilegeRoles = role.getPrivilegeRoles();
//        privilegeRoles.forEach(c -> roleDTO.addPrivileges(c.getPrivilege().getPrivilege()));
//        return roleDTO;
//    }

//    public RoleDTO update(RoleDTO roleDTO) {
//        Role role = addPrivilegeRole(roleDTO);
//        return roleMapper.toDTO(role);
//    }

    public void activateRole(long id) {
        Role role = roleRepository.findById(id).get();
        role.setIsActive(true);
        roleRepository.save(role);
    }

    public void deactivateRole(long id) {
        Role role = roleRepository.findById(id).get();
        role.setIsActive(false);
        roleRepository.save(role);
    }

    public void deleteById(long id) {
        roleRepository.deleteById(id);
    }

//    private Role addPrivilegeRole(RoleDTO roleDTO) {
//        Role role = roleMapper.toEntity(roleDTO);
//        if (roleDTO.getId() != null) {
//            role.setId(roleDTO.getId());
//        }
//        roleRepository.save(role);
//        privilegeRoleRepository.deleteAllByRole(role);
//
//        for (PrivilegeEnum privilegeEnum : roleDTO.getPrivileges()) {
//            savePrivilegeRole(role, privilegeEnum);
//        }
//        savePrivilegeRole(role, PrivilegeEnum.PROFILE_ADMINS);
//
//        return role;
//    }

    private void savePrivilegeRole(Role role, PrivilegeEnum profileAdmins) {
        Privilege byPrivilege = privilegeRepository.findByPrivilege(profileAdmins);
        PrivilegeRole privilegeRole = new PrivilegeRole(role, byPrivilege);
        byPrivilege.addPrivilegeRole(privilegeRole);
        role.addPrivilegeRole(privilegeRole);
        privilegeRoleRepository.save(privilegeRole);
    }
}
