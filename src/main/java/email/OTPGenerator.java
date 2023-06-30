package email;

import java.util.Random;

public class OTPGenerator {
    public static String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000; // Generate a random 6-digit OTP
        return String.valueOf(otp);
    }
}
