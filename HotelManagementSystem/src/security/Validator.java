package security;

import org.jetbrains.annotations.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public boolean isName(@NotNull String name) {
        for(char c : name.toCharArray()) {
            if(!Character.isAlphabetic(c)){
                return false;
            }
        }
        return true;
    }
    public boolean isUserName(@NotNull String username) {
        if(Character.isDigit(username.charAt(0))) {
            return false;
        }

        for(char c : username.toCharArray()) {
            if(!Character.isAlphabetic(c) && !Character.isDigit(c) ){
                return false;
            }
        }
        return true;
    }

    public boolean isEmail(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$");
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }

    public boolean checkPassword(String password) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$").matcher(password).matches();
    }


    public boolean isAddress(String addressText) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[ ])[A-Za-z\\d@ #]{4,}$").matcher(addressText).matches();
    }

    public boolean isPhoneNumber(String phoneText) {
        return Pattern.compile("^\\d{11}$").matcher(phoneText).matches();
    }

    public String errorString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error in provided information. kindly make sure information match the provided criteria\n\n");
        sb.append("1. First name and last name should only contain letters\n");
        sb.append("2. Username cannot start from a digit however it can end and have digit in mid\n");
        sb.append("3. Password : Minimum eight characters, at least one letter, one number and one special character(@$!%*#?&)\n");
        sb.append("4. Phone number: 03xxxxxxxxx format\n");
        sb.append("5. Address can contain letters, numbers and # symbol only\n");
        sb.append("6. Email format : example@example.com\n");
        return sb.toString();

    }
}
