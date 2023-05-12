package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    UserList userList = UserList.getInstance();
    private List<User> users = userList.getUserList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
        users.add(new User("nhatyugioh2@gmail.com", "User1", "password1", "Male"));
        users.add(new User("user2@example.com", "User2", "password2", "Female"));
        userList.setUserList(users);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}