package com.example.user.personaldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectionBody extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_body);
    }
    public void onButtonClick(View view){
        if(view.getId() == R.id.NotesNewBtn)
        {
            Intent i = new Intent(SelectionBody.this, AddNote.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.StoriesNewBtn)
        {
            Intent i = new Intent(SelectionBody.this, AddStory.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.TravelNewBtn)
        {
            Intent i = new Intent(SelectionBody.this, AddTravel.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.PicturesNewBtn)
        {
            Intent i = new Intent(SelectionBody.this, AddPicture.class);
            startActivity(i);
        }
        else if(view.getId() == R.id.FriendsNewBtn)
        {
            Intent i = new Intent(SelectionBody.this, AddFriend.class);
            startActivity(i);
        }
    }
}
