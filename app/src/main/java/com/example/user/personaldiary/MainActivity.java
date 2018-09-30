package com.example.user.personaldiary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
   private DatabaseReference mDatabaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUsers.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
    }

    /*
    private void checkUser() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    Intent mainIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        */

    @Override
    protected void onStart(){

        super.onStart();
        //checkUser();
        mAuth.addAuthStateListener(mAuthListener);
    }



    public void onButtonClick(View view){
        if(view.getId() == R.id.mNotes)
        {
            Intent i = new Intent(MainActivity.this, Note.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.mStories)
        {
            Intent i = new Intent(MainActivity.this, Story.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.mTravel)
        {
            Intent i = new Intent(MainActivity.this, TRAVEL.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.mPictures)
        {
            Intent i = new Intent(MainActivity.this, PICTURE.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.mFriends)
        {
            Intent i = new Intent(MainActivity.this, FRIEND.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.mNew)
        {
            Intent i = new Intent(MainActivity.this, TRAVEL.class);
            startActivity(i);
        }
    }
    //==========================================================================
    //==========================================================================




    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }


    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.action_add){

            startActivity(new Intent(MainActivity.this, SelectionBody.class));

        }

        if (item.getItemId() == R.id.action_logout){
            logout();
        }

        if (item.getItemId() == R.id.action_settings){
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (item.getItemId() == R.id.action_allUSER){
            Intent AlluserIntent = new Intent(MainActivity.this, AddFriend.class);
            startActivity(AlluserIntent);

        }

        //return super.onOptionsItemSelected(item);
        return true;
    }

    private void logout() {
        mAuth.signOut();
    }
    //==========================================================================
    //==========================================================================

}
