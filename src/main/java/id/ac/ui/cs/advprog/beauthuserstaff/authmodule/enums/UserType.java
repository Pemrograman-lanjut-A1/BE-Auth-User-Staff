package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums;


public enum UserType {
    STAFF("STAFF"),
    REGULAR("REGULAR");

    private final String roleType;

    UserType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleType() {
        return roleType;
    }

    public static boolean contains(String value) {
        for (UserType userType : values()) {
            if (userType.getRoleType().equals(value)) {
                return true;
            }
        }
        return false;
    }
}