package com.example.loginregister;

import java.io.Serializable;
import java.util.Random;

public class Otp implements Serializable {
    private String value;
    private long timestamp;
    private static final int EXPIRATION_TIME_SECONDS = 60;

    public Otp() {
        this.value = generateOTP();
        this.timestamp = System.currentTimeMillis() / 1000; // Convert to seconds
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        return (currentTime - timestamp) >= EXPIRATION_TIME_SECONDS;
    }

    public void renewOtp() {
        this.value = generateOTP();
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public String generateOTP() {
        // Generate a random six-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
