package id.ac.ui.cs.advprog.beauthuserstaff.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository.UserRepository;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findByEmail_UserExists_ReturnUser() {
        // Arrange
        String email = "test@example.com";
        User user = User.builder()
                .email(email)
                .username("johndoe")
                .password("Password1!").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> found = userRepository.findByEmail(email);

        // Assert
        assertEquals(user, found.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_UserNotFound_ReturnEmptyOptional() {
        // Arrange
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> found = userRepository.findByEmail(email);

        // Assert
        assertEquals(Optional.empty(), found);
        verify(userRepository, times(1)).findByEmail(email);
    }
}
