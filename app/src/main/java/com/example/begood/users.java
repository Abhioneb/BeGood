/* Created by Abhinav Pandey on 28 March, 2023 at 8 AM */

package com.example.begood;

public class users {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String profilePic;
    private String phone;

    public users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public users(String userId, String userName, String email, String password, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}