package com.example.loginregister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    User user;
    TextView emailTextView;
    TextView usernameTextView;
    TextView genderTextView ;
    TextView logoutTextView;
    ImageView avatarImageView;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        emailTextView = findViewById(R.id.emailTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        genderTextView = findViewById(R.id.genderTextView);
        avatarImageView = findViewById(R.id.avatarImageView);
        logoutTextView= findViewById(R.id.logout_text);
        editProfile = findViewById(R.id.edit_button);

        emailTextView.setText(user.getEmail());
        usernameTextView.setText(user.getUsername());
        genderTextView.setText(user.getGender());
        if(!user.getAvatar().equals("")) {
            new LoadImg().execute(user.getAvatar());
        }

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout();
            }
        });

        editProfile.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent1.putExtra("user", user);
            startActivity(intent1);
        });

    }
    private class LoadImg extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            avatarImageView.setImageBitmap(bitmap);
        }
    }



    private void logout() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}