package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class OtpActivity extends AppCompatActivity {

    private EditText otpEditText;
    private Otp otp;
    UserList userList = UserList.getInstance();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Button verifyButton = findViewById(R.id.verify_button);
        TextView resendOptText = findViewById(R.id.resend_otp);

        otpEditText = findViewById(R.id.otp_edittext);
        // get data form prev page
        Intent intent = getIntent();
        otp = (Otp) intent.getSerializableExtra("otp");
        user = (User) intent.getSerializableExtra("user");

        verifyButton.setOnClickListener(view -> {
            String enteredOtp = otpEditText.getText().toString().trim();
            if (enteredOtp.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
            } else if (enteredOtp.equals(otp.getValue())) {
                if(otp.isExpired()) {
                    Toast.makeText(OtpActivity.this, "Otp has expired, please click resend to renew otp", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(OtpActivity.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                userList.addUser(user);
                Intent intentLogin = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            } else {
                Toast.makeText(OtpActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });

        resendOptText.setOnClickListener(v -> {
            otp.renewOtp();
            String messageBody = "Your OTP is " + otp.getValue() + ". Please enter this OTP to complete your registration.";
            MailSender task = new MailSender(user.getEmail(), messageBody);
            task.execute();
            Toast.makeText(this, "OTP successfully send", Toast.LENGTH_SHORT).show();
        });
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
