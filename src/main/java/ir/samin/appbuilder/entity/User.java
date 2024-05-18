package ir.samin.appbuilder.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Data
public class User extends BaseEntity {


    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "confirm_code")
    private String confirmCode;
    @Column(name = "confirm_code_register_time")
    private LocalDateTime confirmCodeRegisterTime;
    @Column(name = "is_enabled")
    private boolean isEnabled;

}
