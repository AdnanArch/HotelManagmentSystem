package email;

import java.util.Random;

public class OTPGenerator {
    private String generatedOTP;

    public String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000; // Generate a random 6-digit OTP
        generatedOTP = String.valueOf(otp);
        return generatedOTP;
    }

    public String getGeneratedOTP() {
        return generatedOTP;
    }
}
