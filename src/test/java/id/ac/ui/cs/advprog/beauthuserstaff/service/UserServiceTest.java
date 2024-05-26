package id.ac.ui.cs.advprog.beauthuserstaff.service;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void loadUserByUsername_UserExists_ReturnUserDetails() {
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .password("Password1!").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);

        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    void loadUserByUsername_UserNotFound_ThrowUsernameNotFoundException() {
        String username = "test@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        var userDetailsService = userService.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByEmail(username);
    }

}
