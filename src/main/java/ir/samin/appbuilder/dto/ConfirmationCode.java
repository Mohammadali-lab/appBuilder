package ir.samin.appbuilder.dto;

import lombok.Data;

@Data
public class ConfirmationCode {

    private String phoneNumber;

    private String otpCode;
}
