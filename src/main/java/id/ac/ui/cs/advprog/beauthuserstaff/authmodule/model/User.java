package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_user")
public class User implements UserDetails {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private UserType type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getUsername() {
        return email;
    }
}
