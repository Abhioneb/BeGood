package com.example.begood;


import javax.xml.transform.sax.SAXResult;

public class posts {

    private String postId;
    private String userName;
    private String userId;
    private String caption;
    private String requestType;
    private double latitude;
    private double longitude;
    private String address;
    private long timestamp;
    private String imageUrl;

    public String getUserName() {
        return userName;
    }

    public posts(String postId, String userName, String userId, String caption, String requestType, double latitude, double longitude, String address, long timestamp, String imageUrl) {
        this.postId = postId;
        this.userName = userName;
        this.userId = userId;
        this.caption = caption;
        this.requestType = requestType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }


    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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


    public posts() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class). Don't forget this to add.
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
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
