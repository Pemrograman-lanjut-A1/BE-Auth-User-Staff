package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.enums;


public enum UserType {
    STAFF("STAFF"),
    REGULAR("REGULAR");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public static boolean contains(String value) {
        for (UserType userType : values()) {
            if (userType.getUserType().equals(value)) {
                return true;
            }
        }
        return false;
    }
}