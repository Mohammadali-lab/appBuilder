package ir.samin.appbuilder.service;

import ir.samin.appbuilder.dao.AdminRepository;
import ir.samin.appbuilder.dto.AdminCreationDTO;
import ir.samin.appbuilder.dto.AdminFullDTO;
import ir.samin.appbuilder.dto.mapper.AdminMapper;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.entity.PrivilegeEnum;
import ir.samin.appbuilder.entity.Role;
import ir.samin.appbuilder.exception.GeneralRuntimeException;
import ir.samin.appbuilder.exception.UserNotAllowedException;
import ir.samin.appbuilder.security.CustomAdminDetails;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private AdminRepository adminRepository;

    private RoleService roleService;

    private final AdminMapper adminMapper = Mappers.getMapper(AdminMapper.class);

    public AdminService(AdminRepository adminRepository, RoleService roleService) {
        this.adminRepository = adminRepository;
        this.roleService = roleService;
    }


    public AdminFullDTO createAdminFullDTO(AdminCreationDTO adminCreationDTO){
        Role role = roleService.findByEnglishName(adminCreationDTO.getRole());
        return adminMapper.toFullDTO(create(adminMapper.toEntity(adminCreationDTO), role));

    }

    public Admin create(Admin admin, Role role) {
        if (admin.getPassword() == null || admin.getPassword().equals("")) {
            throw new GeneralRuntimeException("Invalid Password");
        }
        admin.setActive(true);
        role.addAdmin(admin);
        checkDuplicateAdmin(admin, admin.getUsername());

        admin = adminRepository.save(admin);
        return admin;
    }

    private void checkDuplicateAdmin(Admin admin, String mobileNumber) {
        Optional<Admin> byMobileNumber = adminRepository.findByUsername(mobileNumber.toLowerCase());
        if (byMobileNumber.isPresent() && !byMobileNumber.get().equals(admin)) {
            throw new GeneralRuntimeException("MobileNumber Exists");
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (!admin.isPresent()) {
            return null;
        } else if (!admin.get().isEnabled()) {
            throw new UserNotAllowedException("You Are Not Allowed To Login! ");
        }
        return new CustomAdminDetails(admin.get());
    }

    public AdminFullDTO findByPhoneNumber(String phoneNumber) {
        Admin admin = adminRepository.findByUsername(phoneNumber).get();
        return getAdminFullDTO(admin);
    }


    private AdminFullDTO getAdminFullDTO(Admin admin) {
        AdminFullDTO adminFullDTO = adminMapper.toFullDTO(admin);
        List<PrivilegeEnum> privilegeEnums = new ArrayList<>();
        admin.getRole().getPrivilegeRoles().forEach(privilegeRole -> {
            privilegeEnums.add(privilegeRole.getPrivilege().getPrivilege());
        });
        adminFullDTO.getRole().setPrivileges(privilegeEnums);
        return adminFullDTO;
    }

}
