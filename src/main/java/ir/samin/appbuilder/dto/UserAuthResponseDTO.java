package ir.samin.appbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthResponseDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String token;
    private String message;
}
