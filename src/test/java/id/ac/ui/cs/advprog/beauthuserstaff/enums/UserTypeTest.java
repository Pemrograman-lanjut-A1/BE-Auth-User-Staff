package id.ac.ui.cs.advprog.beauthuserstaff.enums;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums.UserType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void getUserType_ShouldReturnCorrectType() {
        assertEquals("STAFF", UserType.STAFF.getUserType());
        assertEquals("REGULAR", UserType.REGULAR.getUserType());
    }

    @Test
    void contains_ShouldReturnTrueForValidType() {
        assertTrue(UserType.contains("STAFF"));
        assertTrue(UserType.contains("REGULAR"));
    }

    @Test
    void contains_ShouldReturnFalseForInvalidType() {
        assertFalse(UserType.contains("ADMIN"));
        assertFalse(UserType.contains("GUEST"));
    }
}

