/* Created by Abhinav Pandey on 28 March, 2023 at 6:02 AM */

package com.example.begood;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class adapter extends FirebaseRecyclerAdapter<posts, adapter.myView> {

    public adapter(@NonNull FirebaseRecyclerOptions<posts> options) {
        super(options);
    }

    public class myView extends RecyclerView.ViewHolder {

        TextView userName, caption, timeAgo;
        ImageView image;
        CircleImageView profilePic;
        Button donate;

        public myView(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            profilePic = itemView.findViewById(R.id.profilePic);
            userName = itemView.findViewById(R.id.userName);
            caption = itemView.findViewById(R.id.caption);
            timeAgo = itemView.findViewById(R.id.timeStampTxt);
            donate = itemView.findViewById(R.id.donate);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull adapter.myView holder, int position, @NonNull posts post) {

        // calculating time spent since post was made.
        if (position < getItemCount()) {

            String timeSpent = "";
            {
                long postTimestamp = post.getTimestamp();
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

            String userId = post.getUserId();

            // binding the views
            {
                holder.userName.setText(post.getUserName());
                holder.caption.setText(post.getCaption());
                holder.timeAgo.setText(timeSpent);
                Glide.with(holder.image.getContext()).load(post.getImageUrl()).into(holder.image);
//        Glide.with(holder.profilePic.getContext()).load(profilePicture).into(holder.profilePic);
            }

            // click listeners for donate button
            {
                holder.donate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(holder.donate.getContext(), pickUp.class);
                        intent.putExtra("requestId", userId);
                        intent.putExtra("imageUri", post.getImageUrl());
                        intent.putExtra("postId", post.getPostId());
                        holder.donate.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public adapter.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new adapter.myView(view);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the Firebase Realtime Database
        return getSnapshots().size();
    }

}


