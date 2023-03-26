package com.example.begood;

public class Post {

    private String postId;
    private String userId;
    private String userName;
    private String caption;
    private double latitude;
    private double longitude;
    private String address;
    private long timestamp;
    private String imageUrl;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String postId, String userId, String userName, String caption, double latitude, double longitude, String address,long timestamp, String imageUrl) {
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.caption = caption;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.address=address;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getCaption() {
        return caption;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

class User {
    private String userId;
    private String userName;
    private String email;
    private String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String userName, String email, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
