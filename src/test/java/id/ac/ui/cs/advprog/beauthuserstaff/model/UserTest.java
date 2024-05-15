package id.ac.ui.cs.advprog.beauthuserstaff.model;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .type(UserType.REGULAR)
                .build();
    }

    @Test
    public void testUserBuilder() {
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(UserType.REGULAR, user.getType());
    }

    @Test
    public void testUserAuthorities() {
        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority(UserType.REGULAR.name())));
    }

    @Test
    public void testUserAccountStatus() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testUserSettersAndGetters() {
        user.setUsername("jane_doe");
        user.setEmail("jane@example.com");
        user.setPassword("newpassword");
        user.setType(UserType.STAFF);

        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals(UserType.STAFF, user.getType());
    }

    @Test
    public void testUserEquality() {
        User sameUser = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .type(UserType.REGULAR)
                .build();

        User differentUser = User.builder()
                .username("jane_doe")
                .email("jane@example.com")
                .password("password456")
                .type(UserType.REGULAR)
                .build();

        assertEquals(user, sameUser);
        assertNotEquals(user, differentUser);
    }
}
