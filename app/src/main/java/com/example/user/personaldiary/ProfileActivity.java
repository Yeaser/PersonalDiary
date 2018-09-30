package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {


    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mProfileFriendsCount;
    private Button mProfileSendReqButton;
    private Button mProfile_decline_req_btn;

    private ProgressDialog mProgressProfile;

    private DatabaseReference mUserProfileDatabase;

    private DatabaseReference mFriendRequestDatabase;  //FOR STORING FRIEND REQUEST DATA
    private FirebaseUser mUserForFriendReq;     // FOR GETTING THE CURRENT USER ID TO STORE IN DATABASE

    private DatabaseReference mFriendToBeDatabase;

    private String mCurrentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // GETTING USER ID OF EACH USER TO FIND THEM IN THE DATABASE INDIVIDUALLY...........
        final String user_ID = getIntent().getStringExtra("user_id");  //this id is whom we are sending friend request

        mUserProfileDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_ID);

        mFriendToBeDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Req");

        mUserForFriendReq = FirebaseAuth.getInstance().getCurrentUser();  // this is the current user

        mProfileImage = (ImageView) findViewById(R.id.Tprofile_image);
        mProfileName = (TextView) findViewById(R.id.Tprofile_name);
        mProfileStatus = (TextView) findViewById(R.id.Tprofile_status);
        mProfileFriendsCount = (TextView) findViewById(R.id.Tprofile_totalFriends);
        mProfileSendReqButton = (Button) findViewById(R.id.Tprofile_send_req_btn);
        mProfile_decline_req_btn = (Button) findViewById(R.id.Tprofile_decline_req_btn);

        mCurrentState = "Not_Friend";


        mProgressProfile = new ProgressDialog(this);
        mProgressProfile.setTitle("Loading User Data");
        mProgressProfile.setMessage("Please wait while we load the user data");
        mProgressProfile.setCanceledOnTouchOutside(false);
        mProgressProfile.show();



        mUserProfileDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_ID);

        mUserProfileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("Name").getValue().toString();
                String display_status = dataSnapshot.child("Status").getValue().toString();
                String display_image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(display_status);

                Picasso.with(ProfileActivity.this).load(display_image).placeholder(R.drawable.ic_account_circle_black_24dp).into(mProfileImage);

                //========================================= FRIEND LIST / REQUEST ===========================================================================
                mFriendRequestDatabase.child(mUserForFriendReq.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_ID)){

                            String req_type = dataSnapshot.child(user_ID).child("request_type").getValue().toString();
                            if(req_type.equals("I_received_request")){

                                mCurrentState = "req_received";
                                mProfileSendReqButton.setText("ACCEPT FRIEND REQUEST");

                            }else if(req_type.equals("I_sent_request")){

                                mCurrentState = "req_sent";
                                mProfileSendReqButton.setText("CANCEL FRIEND REQUEST");
                            }

                            mProgressProfile.dismiss();

                        }else{

                            mFriendToBeDatabase.child(mUserForFriendReq.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(user_ID)){

                                        mCurrentState = "Friends";
                                        mProfileSendReqButton.setText("UNFRIEND THIS PERSON");

                                    }

                                    mProgressProfile.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressProfile.dismiss();
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        
                        mProgressProfile.dismiss();
                    }
                });

                //====================================================================================================================


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //======================================== SEND BUTTON ===================================================

        mProfileSendReqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProfileSendReqButton.setEnabled(false);


                // ------------------------------------------- NOT FRIEND STATE -------------------------------------------------------

                if(mCurrentState.equals("Not_Friend")){

                    mFriendRequestDatabase.child(mUserForFriendReq.getUid()).child(user_ID).child("request_type")
                            .setValue("I_sent_request").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mFriendRequestDatabase.child(user_ID).child(mUserForFriendReq.getUid()).child("request_type")
                                        .setValue("I_received_request").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        mCurrentState = "req_sent";
                                        mProfileSendReqButton.setText("CANCEL FRIEND REQUEST");

                                        Toast.makeText(ProfileActivity.this, "Request Sent Successfully", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }else{

                                Toast.makeText(ProfileActivity.this, "Failed Sending Request", Toast.LENGTH_LONG).show();
                            }
                            mProfileSendReqButton.setEnabled(true);
                        }
                    });
                }

                // ------------------------------------------- CANCEL REQUEST STATE -------------------------------------------------------

                if(mCurrentState.equals("req_sent")){

                    mFriendRequestDatabase.child(mUserForFriendReq.getUid()).child(user_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task0) {
                            if(task0.isSuccessful()){

                                mFriendRequestDatabase.child(user_ID).child(mUserForFriendReq.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){

                                            mProfileSendReqButton.setEnabled(true);
                                            mCurrentState = "Not_Friend";
                                            mProfileSendReqButton.setText("SEND FRIEND REQUEST");

                                        } else{

                                            Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }


                //=================================== REQUEST RECEIVE STATE ================================
                 if(mCurrentState.equals("req_received")){

                     final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                     mFriendToBeDatabase.child(mUserForFriendReq.getUid()).child(user_ID).setValue(currentDate)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task3) {
                                if(task3.isSuccessful()){

                                    mFriendToBeDatabase.child(user_ID).child(mUserForFriendReq.getUid()).setValue(currentDate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task4) {
                                            if(task4.isSuccessful()){


                                                mFriendRequestDatabase.child(mUserForFriendReq.getUid()).child(user_ID).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task0) {
                                                        if(task0.isSuccessful()){

                                                            mFriendRequestDatabase.child(user_ID).child(mUserForFriendReq.getUid()).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    if(task1.isSuccessful()){

                                                                        mProfileSendReqButton.setEnabled(true);
                                                                        mCurrentState = "Friends";
                                                                        mProfileSendReqButton.setText("UNFRIEND THIS PERSON");

                                                                    } else{

                                                                        Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                                                                    }
                                                                }
                                                            });


                                                        } else {
                                                            Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });


                                            }else{

                                            }
                                        }
                                    });

                                } else{




                                }
                         }
                     });

                 }


            }
        });
        //======================================== END SEND BUTTON ===================================================


        /*
        mProfile_decline_req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProfile_decline_req_btn.setEnabled(false);

                if(mCurrentState.equals("Friends")){

                    mFriendRequestDatabase.child(mUserForFriendReq.getUid()).child(user_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task0) {
                            if(task0.isSuccessful()){

                                mFriendRequestDatabase.child(user_ID).child(mUserForFriendReq.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){

                                            mProfileSendReqButton.setEnabled(true);
                                            mCurrentState = "Not_Friend";
                                            mProfileSendReqButton.setText("SEND FRIEND REQUEST");

                                        } else{

                                            Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });


                            } else {
                                Toast.makeText(ProfileActivity.this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
                }
        });




         */

    }
}
