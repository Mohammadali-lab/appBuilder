package ir.samin.appbuilder.dao;

import ir.samin.appbuilder.entity.PrivilegeRole;
import ir.samin.appbuilder.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRoleRepository extends JpaRepository<PrivilegeRole, Long> {

    void deleteAllByRole(Role role);
}
