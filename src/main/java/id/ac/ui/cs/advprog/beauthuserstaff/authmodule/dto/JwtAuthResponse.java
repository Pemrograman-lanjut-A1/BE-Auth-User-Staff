package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class JwtAuthResponse {
    private String token;
    private String refreshToken;
}
