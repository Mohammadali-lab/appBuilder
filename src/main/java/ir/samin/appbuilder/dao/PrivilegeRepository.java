package ir.samin.appbuilder.dao;

import ir.samin.appbuilder.entity.Privilege;
import ir.samin.appbuilder.entity.PrivilegeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByPrivilege(PrivilegeEnum privilege);
}
