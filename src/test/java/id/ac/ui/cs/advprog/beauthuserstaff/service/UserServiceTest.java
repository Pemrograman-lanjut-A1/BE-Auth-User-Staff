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
        String username = "johndoe";
        User user = User.builder()
                .email(email)
                .username(username)
                .password("Password1!").build();

        when(userRepository.findByEmail(username)).thenReturn(Optional.ofNullable(user));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(userRepository, times(1)).findByEmail(username);
    }


    @Test
    void loadUserByUsername_UserNotFound_ThrowUsernameNotFoundException() {
        // Arrange
        String username = "test@example.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.userDetailsService().loadUserByUsername(username));

        verify(userRepository, times(1)).findByEmail(username);
    }
}
