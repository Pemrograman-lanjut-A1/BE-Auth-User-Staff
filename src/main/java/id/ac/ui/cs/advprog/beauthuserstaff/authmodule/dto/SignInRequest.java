package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
