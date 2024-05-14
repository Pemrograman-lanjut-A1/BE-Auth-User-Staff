package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RefreshTokenRequest {
    private String token;
}
