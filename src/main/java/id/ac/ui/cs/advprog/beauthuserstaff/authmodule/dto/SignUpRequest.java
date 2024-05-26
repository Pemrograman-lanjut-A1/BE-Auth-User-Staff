package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String username;
    private String password;
}
