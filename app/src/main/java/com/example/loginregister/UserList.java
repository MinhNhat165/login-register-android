package com.example.loginregister;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private static UserList instance = null;
    private List<User> userList;
    private UserList() {
        userList = new ArrayList<>();
    }

    public static UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public boolean isUserExists(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public User findUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User userToUpdate) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user.getEmail().equals(userToUpdate.getEmail())) {
                userList.set(i, userToUpdate);
                return;
            }
        }
    }

    public void addUser(User user) {
        this.userList.add(user);
    }



}
