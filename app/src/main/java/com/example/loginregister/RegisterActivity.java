package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText usernameEditText;
    private RadioGroup genderRadioGroup;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView loginMessageTextView;
    UserList userList = UserList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailEditText = findViewById(R.id.email_edittext);
        usernameEditText = findViewById(R.id.username_edittext);
        genderRadioGroup = findViewById(R.id.gender_radiogroup);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        loginMessageTextView = findViewById(R.id.login_message_textview);;
        genderRadioGroup.check(R.id.male_radiobutton);
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String gender = getSelectedGender();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            } else {
                if (userList.isUserExists(email)) {
                    Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Invalid email address");
                emailEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(username)) {
                usernameEditText.setError("Username is required");
                usernameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(gender)) {
                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters long");
                passwordEditText.requestFocus();
                return;
            }

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

            Otp otp = new Otp();
            String messageBody = "Your OTP is " + otp.getValue() + ". Please enter this OTP to complete your registration.";
            MailSender task = new MailSender(email, messageBody);
            task.execute();
            User newUser = new User(email, username, password, gender);
            Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
            intent.putExtra("otp", otp);
            intent.putExtra("user", newUser);
            startActivity(intent);
        });

        loginMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getSelectedGender() {
        RadioButton maleRadioButton = findViewById(R.id.male_radiobutton);
        if (maleRadioButton.isChecked()) {
            return "Male";
        }
        RadioButton femaleRadioButton = findViewById(R.id.female_radiobutton);
        if (femaleRadioButton.isChecked()) {
            return "Female";
        }
        return "";
    }


}
