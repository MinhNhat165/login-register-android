package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPassActivity extends AppCompatActivity {
    Otp otp;
    String email;


    TextView otpEditText;
    TextView passwordEditText;
    TextView confirmPasswordEditText;
    TextView resendOtpText;
    Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset_pass);
        otpEditText = findViewById(R.id.otp_edit_text);
        passwordEditText = findViewById(R.id.new_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.new_confirm_password_edit_text);
        resendOtpText = findViewById(R.id.resend_otp_reset);

        Intent intent = getIntent();
        otp = (Otp) intent.getSerializableExtra("otp");
        email = intent.getStringExtra("email");

        resetButton = findViewById(R.id.reset_password_button);

        resetButton.setOnClickListener(v -> {
            String enteredOtp = otpEditText.getText().toString().trim();
            if (enteredOtp.isEmpty()) {
                Toast.makeText(ResetPassActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                return;
            } else if (!enteredOtp.equals(otp.getValue())) {
                Toast.makeText(ResetPassActivity.this, "OTP verification incorrect", Toast.LENGTH_SHORT).show();
                return;
            } else if(otp.isExpired()) {
                Toast.makeText(ResetPassActivity.this, "Otp has expired, please click resend to renew otp", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = passwordEditText.getText().toString().trim();

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters long");
                passwordEditText.requestFocus();
                return;
            }

            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordEditText.setError("Confirm password is required");
                confirmPasswordEditText.requestFocus();
                return;
            }

            if (!confirmPassword.equals(password)) {
                confirmPasswordEditText.setError("Passwords do not match");
                confirmPasswordEditText.requestFocus();
                return;
            }

            Toast.makeText(this, "Your password was reset successfully", Toast.LENGTH_SHORT).show();
            UserList userList = UserList.getInstance();
            User user = userList.findUserByEmail(email);
            user.setPassword(password);
            userList.updateUser(user);

            Intent intentLogin = new Intent(ResetPassActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        });

        resendOtpText.setOnClickListener(v -> {
            otp.renewOtp();
            String messageBody = "Enter the code: " + otp.getValue() + " to reset password";
            MailSender task = new MailSender(email, messageBody);
            task.execute();
            Toast.makeText(this, "OTP successfully send", Toast.LENGTH_SHORT).show();
        });
    }
}