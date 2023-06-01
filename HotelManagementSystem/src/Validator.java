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
}
