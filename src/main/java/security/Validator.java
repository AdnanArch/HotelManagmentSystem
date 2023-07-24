package security;

import java.util.regex.Pattern;

public class Validator {
    private static final String NAME_PATTERN = "^[a-zA-Z]+$";
    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$";
    private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*--).{8,}$";
    private static final String ADDRESS_PATTERN = "^[a-zA-Z0-9#\\s]+$";
    private static final String PHONE_PATTERN = "^\\d{11}$";

    public String validateCustomerData(String firstName, String lastName, String userName, String email, String password, String address, String phone, String emergencyContact) {
        if (isEmpty(firstName)) {
            return "First name is required.";
        } else if (!isFirstName(firstName)) {
            return "First name should contain only letters.";
        } else if (isEmpty(lastName)) {
            return "Last name is required.";
        } else if (!isLastName(lastName)) {
            return "Last name should contain only letters.";
        } else if (isEmpty(userName)) {
            return "Username is required.";
        } else if (!isUserName(userName)) {
            return "Username should start with a letter and can contain letters and digits.";
        } else if (isEmpty(email)) {
            return "Email is required.";
        } else if (!isEmail(email)) {
            return "Invalid email format.";
        } else if (isEmpty(password)) {
            return "Password is required.";
        } else if (!checkPassword(password)) {
            return "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.";
        } else if (isEmpty(address)) {
            return "Address is required.";
        } else if (!isAddress(address)) {
            return "Address can contain letters, numbers, and the # symbol only.";
        } else if (isEmpty(phone)) {
            return "Phone number is required.";
        } else if (!isPhoneNumber(phone)) {
            return "Invalid phone number format. It should be 03xxxxxxxxx.";
        } else if (!isEmergencyPhoneNumber(emergencyContact)) {
            return "Invalid emergency phone number format. It should be 03xxxxxxxxx.";
        }
        return null; // Data is valid
    }

    private boolean isEmergencyPhoneNumber(String emergencyContact) {
        return Pattern.matches(PHONE_PATTERN, emergencyContact);
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isFirstName(String name) {
        return Pattern.matches(NAME_PATTERN, name);
    }

    private boolean isLastName(String name) {
        return Pattern.matches(NAME_PATTERN, name);
    }

    private boolean isUserName(String username) {
        return Pattern.matches(USERNAME_PATTERN, username);
    }

    private boolean isEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    private boolean checkPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    private boolean isAddress(String addressText) {
        return Pattern.matches(ADDRESS_PATTERN, addressText);
    }

    private boolean isPhoneNumber(String phoneText) {
        return Pattern.matches(PHONE_PATTERN, phoneText);
    }
}
