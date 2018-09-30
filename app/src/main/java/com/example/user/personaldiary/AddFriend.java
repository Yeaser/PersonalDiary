package com.example.user.personaldiary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriend extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference mNewUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


        mNewUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


    }


    //================================================================================================================================
    //*

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mNewUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.NameSet(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                //viewHolder.NameSet("WHAT IS THE PROBLEM");
                viewHolder.setUserThumbImages(model.getThumb_image(), getApplicationContext());

                final String user_id_generate = getRef(position).getKey();

                viewHolder.mUserView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //getting key to view different users
                        Intent profileIntent = new Intent(AddFriend.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id_generate);
                        startActivity(profileIntent);


                    }
                });
            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    //====================================================================================================================================

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mUserView;

        public UsersViewHolder(View itemView){
            super(itemView);
            mUserView = itemView;
        }

        public void NameSet(String Name){
            TextView userNAMEview = (TextView) mUserView.findViewById(R.id.user_single_name);
            userNAMEview.setText(Name);
        }
        public void setUserStatus(String status){
            TextView userStatusView = (TextView) mUserView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }

        public void  setUserThumbImages(String Thumb_Images, Context ctx){

            CircleImageView userImageView = (CircleImageView) mUserView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(Thumb_Images).placeholder(R.drawable.ic_account_circle_black_24dp).into(userImageView);
        }

    }

    //*/
    //==========================================================================================================================
}
