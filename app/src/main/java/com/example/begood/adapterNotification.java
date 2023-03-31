/* Created by Abhinav Pandey on 30 March, 2023 at 6:02 AM */

package com.example.begood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapterNotification extends FirebaseRecyclerAdapter<donationOffers, adapterNotification.myView> {

    public adapterNotification(@NonNull FirebaseRecyclerOptions<donationOffers> options) {
        super(options);
    }

    public class myView extends RecyclerView.ViewHolder {

        TextView notification;
        ImageButton postPhoto;
        CircleImageView donorProfile;

        public myView(@NonNull View itemView) {
            super(itemView);

            notification = itemView.findViewById(R.id.notificationTxt);
            postPhoto = itemView.findViewById(R.id.postPhoto);
            donorProfile = itemView.findViewById(R.id.donorProfile);

        }
    }

    @Override
    protected void onBindViewHolder(@NonNull adapterNotification.myView holder, int position, @NonNull donationOffers model) {

        if (position < getItemCount()) {
            String timeSpent = "";
            String donorName = model.getDonorName();

            {
                long postTimestamp = model.getTimestamp();
                long currentTime = System.currentTimeMillis();
                long timeSincePost = currentTime - postTimestamp;

                long secondsSincePost = timeSincePost / 1000;
                long minutesSincePost = secondsSincePost / 60;
                long hoursSincePost = minutesSincePost / 60;
                long daysSincePost = hoursSincePost / 24;
                long monthsSincePost = daysSincePost / 30;
                long yearsSincePost = monthsSincePost / 12;

                if (secondsSincePost < 60)
                    timeSpent = Long.toString(secondsSincePost) + " seconds ago";
                else if (minutesSincePost >= 1 && minutesSincePost < 60)
                    timeSpent = Long.toString(minutesSincePost) + " minutes ago";
                else if (hoursSincePost >= 1 && hoursSincePost < 24)
                    timeSpent = Long.toString(hoursSincePost) + " hours ago";
                else if (daysSincePost >= 1 && daysSincePost < 30)
                    timeSpent = Long.toString(daysSincePost) + " days ago";
                else if (monthsSincePost >= 1 && monthsSincePost < 12)
                    timeSpent = Long.toString(monthsSincePost) + " months ago";
                else if (yearsSincePost >= 1)
                    timeSpent = Long.toString(yearsSincePost) + " years ago";

            }


            String notificationText = donorName + " is willing to donate you food on " + model.getDate() + " at " + model.getTime() + "." + donorName + " is providing " +
                    model.getQuantity() + "kg food cooked food consisting of " + model.getFood() + ".You can pick food at " + model.getLocation() +
                    ".For further clarifications, you can message " + model.getDonorName() + ".  " + timeSpent;

            String fullString = notificationText;
            String boldString = donorName;



            // Remember SpannableString builder is used to build strings with options to edit them visually.
            SpannableStringBuilder builder = new SpannableStringBuilder();

            edit(fullString,boldString,builder);
            edit(fullString, model.getDate(),builder);
            edit(fullString,model.getTime(),builder);
            edit(fullString, model.getQuantity(),builder);
            edit(fullString, model.getFood(),builder);
            edit(fullString, model.getLocation(),builder);
            editColor(fullString,timeSpent,builder);



//             this part is used to bold all instances of a substring.
//            while (index != -1) {
//                // Find the start and end index of the substring to be bolded
//                int startIndex = index;
//                int endIndex = startIndex + boldString.length();
//
//                // Set the text style of the substring to bold
//                builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                // Find the next occurrence of the substring
//                index = fullString.indexOf(boldString, endIndex);
//            }

            holder.notification.setText(builder);
            Glide.with(holder.postPhoto.getContext()).load(model.getImageUri()).into(holder.postPhoto);
//            Glide.with(holder.donorProfile.getContext()).load(model.)


        }
    }

    public void edit(String fullString,String boldString,SpannableStringBuilder builder){

        if(String.valueOf(builder).equals("")) builder.append(fullString);

        // start and end index of the substring to be bolded
        int startIndex = fullString.indexOf(boldString);
        int endIndex = startIndex + boldString.length();

        // Set the text style of the substring to bold
        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fullString= String.valueOf(builder);
    }
    public void editColor(String fullString,String boldString,SpannableStringBuilder builder){

        int lightGreyColor = Color.rgb(128, 128, 128);   // You can use the RGB color method to create a specific color.
        if(String.valueOf(builder).equals("")) builder.append(fullString);

        // start and end index of the substring to be bolded
        int startIndex = fullString.indexOf(boldString);
        int endIndex = startIndex + boldString.length();

        // Set the text style of the substring to bold
        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(lightGreyColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fullString= String.valueOf(builder);
    }

    @NonNull
    @Override
    public adapterNotification.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new adapterNotification.myView(view);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the Firebase Realtime Database
        return getSnapshots().size();
    }


}



