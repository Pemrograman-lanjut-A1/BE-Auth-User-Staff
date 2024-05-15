package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String username;
    private String password;
}
