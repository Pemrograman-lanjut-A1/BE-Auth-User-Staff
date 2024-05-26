package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtAuthResponse {
    private String token;
    private String refreshToken;
}
