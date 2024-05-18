package ir.samin.appbuilder.service;

import ir.samin.appbuilder.dao.AdminRepository;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.exception.UserNotAllowedException;
import ir.samin.appbuilder.security.CustomAdminDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AdminService {

    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
    
}
