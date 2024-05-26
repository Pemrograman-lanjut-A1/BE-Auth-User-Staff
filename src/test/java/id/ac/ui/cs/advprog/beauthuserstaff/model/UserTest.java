package id.ac.ui.cs.advprog.beauthuserstaff.model;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class UserTest {

    private User user;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = User.builder()
                .username("john_doe")
                .email("john@example.com")
                .password("password123")
                .type(UserType.REGULAR)
                .build();
    }

    @Test
    void testUserBuilder() {
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(UserType.REGULAR, user.getType());
    }

    @Test
    void testUserAuthorities() {
        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority(UserType.REGULAR.name())));
    }

    @Test
    void testUserAccountStatus() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserSettersAndGetters() {
        user.setUsername("jane_doe");
        user.setEmail("jane@example.com");
        user.setPassword("newpassword");
        user.setType(UserType.STAFF);

        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertEquals(UserType.STAFF, user.getType());
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }
}
