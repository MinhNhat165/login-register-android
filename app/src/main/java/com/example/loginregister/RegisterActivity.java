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

    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private RadioGroup mGenderRadioGroup;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private TextView loginMessageTextView;
    UserList userList = UserList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmailEditText = findViewById(R.id.email_edittext);
        mUsernameEditText = findViewById(R.id.username_edittext);
        mGenderRadioGroup = findViewById(R.id.gender_radiogroup);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        loginMessageTextView = findViewById(R.id.login_message_textview);;
        mGenderRadioGroup.check(R.id.male_radiobutton);
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> {
            String email = mEmailEditText.getText().toString().trim();
            String username = mUsernameEditText.getText().toString().trim();
            String gender = getSelectedGender();
            String password = mPasswordEditText.getText().toString();
            String confirmPassword = mConfirmPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                mEmailEditText.setError("Email is required");
                mEmailEditText.requestFocus();
                return;
            } else {
                if (userList.isUserExists(email)) {
                    Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailEditText.setError("Invalid email address");
                mEmailEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(username)) {
                mUsernameEditText.setError("Username is required");
                mUsernameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(gender)) {
                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPasswordEditText.setError("Password is required");
                mPasswordEditText.requestFocus();
                return;
            }

            if (password.length() < 6) {
                mPasswordEditText.setError("Password must be at least 6 characters long");
                mPasswordEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                mConfirmPasswordEditText.setError("Confirm password is required");
                mConfirmPasswordEditText.requestFocus();
                return;
            }

            if (!confirmPassword.equals(password)) {
                mConfirmPasswordEditText.setError("Passwords do not match");
                mConfirmPasswordEditText.requestFocus();
                return;
            }

            String otp = generateOTP();
            String messageBody = "Your OTP is " + otp + ". Please enter this OTP to complete your registration.";
            SendEmailTask task = new SendEmailTask(email, messageBody);
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

    private String generateOTP() {
        // Generate a random six-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    private class SendEmailTask extends AsyncTask<Void, Void, Void> {
        private String recipientEmail;
        private String messageBody;

        public SendEmailTask(String recipientEmail, String messageBody) {
            this.recipientEmail = recipientEmail;
            this.messageBody = messageBody;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String from = "devemail301101@gmail.com";
                String password = "ovzvfjsndwatxfoy";
                // Set up properties for the email server
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Authenticate with the email server
                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

                // Compose the email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Your OTP");
                message.setText(messageBody);

                // Send the email message
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
