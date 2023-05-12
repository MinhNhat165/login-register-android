package com.example.loginregister;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private RadioGroup genderRadioGroup;
    private Button saveButton;
    private User user;
    private ImageView avatarEditImageView;
    Uri newImgUri;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        usernameEditText = findViewById(R.id.username_edit_text);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        saveButton = findViewById(R.id.save_button);
        avatarEditImageView = findViewById(R.id.avatar_edit_image_view);
        storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        usernameEditText.setText(user.getUsername());
        if (user.getGender().equals("Male")) {
            genderRadioGroup.check(R.id.male_radiobutton);
        } else if (user.getGender().equals("Female")) {
            genderRadioGroup.check(R.id.female_radiobutton);
        }
        avatarEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContent.launch("image/*");
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = usernameEditText.getText().toString();

                if(newUsername.isEmpty()) {
                    usernameEditText.setError("username is required");
                }
                String gender = getSelectedGender();
                user.setUsername(newUsername);
                user.setGender(gender);
                uploadImage();

            }
        });
    }

    private void uploadImage() {
        if(newImgUri != null) {
            StorageReference reference = storage.getReference().child("images/" + UUID.randomUUID().toString());
            reference.putFile(newImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Task<Uri> downloadUrl = reference.getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                user.setAvatar(imageUrl);
                                UserList.getInstance().updateUser(user);
                                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result !=null) {
                avatarEditImageView.setImageURI(result);
                newImgUri = result;
            }
        }
    });


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