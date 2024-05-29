package ir.samin.appbuilder.dto;


import ir.samin.appbuilder.entity.PrivilegeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Long id;

    private String englishName;

    private String persianName;

    private String iconName;

    private String color;

    private Boolean isActive;

    private String createdAtDateTime;

    private List<PrivilegeEnum> privileges = new ArrayList<>();

    public void addPrivileges(PrivilegeEnum privilege) {
        privileges.add(privilege);
    }
}
