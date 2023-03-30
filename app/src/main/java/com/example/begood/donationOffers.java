/* Created by Abhinav Pandey on 29 March, 2023 at 7:25 AM */

package com.example.begood;

public class donationOffers {

    private String notificationId;
    private String donorId;
    private String requestId;
    private String postId;
    private String donorName;
    private String date;
    private String time;
    private String food;
    private String quantity;
    private String location;
    private long timestamp;
    private String imageUri;


    public donationOffers() {
    }

    public donationOffers(String offerId, String donorId, String requestId, String postId, String donorName, String date, String time, String food, String quantity, String location, long timestamp, String imageUri) {
        this.notificationId = offerId;
        this.donorId = donorId;
        this.requestId = requestId;
        this.postId = postId;
        this.donorName = donorName;
        this.date = date;
        this.time = time;
        this.food = food;
        this.quantity = quantity;
        this.location = location;
        this.timestamp = timestamp;
        this.imageUri = imageUri;
    }

    public String getOfferId() {
        return notificationId;
    }

    public String getDonorId() {
        return donorId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFood() {
        return food;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public String getPostId() {
        return postId;
    }

    public String getImageUri() {
        return imageUri;
    }
}
