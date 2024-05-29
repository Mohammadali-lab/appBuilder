package ir.samin.appbuilder.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminFullDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String newPassword;

    private String email;


    private String mobileNumber;


    private Boolean isActive;

    private RoleDTO role;

    private String createdAtDateTime;
}
