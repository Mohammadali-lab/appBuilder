package ir.samin.appbuilder.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleOrderBy {

    IS_ACTIVE("isActive"),
    CREATED_AT_DATE_TIME("createdAtDateTime"),
    PERSIAN_NAME("persianName"),
    ENGLISH_NAME("englishName");


    private final String name;
}
