package com.example.user.personaldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Story extends AppCompatActivity {

    private RecyclerView mStoryList;
    private DatabaseReference mStoryDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        mStoryDatabase = FirebaseDatabase.getInstance().getReference().child("STORY");

        mStoryList = (RecyclerView) findViewById(R.id.story_list);
        mStoryList.setHasFixedSize(true);
        mStoryList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<story_des, StoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<story_des, StoryViewHolder>(

                story_des.class,
                R.layout.story_row,
                StoryViewHolder.class,
                mStoryDatabase

        ) {
            @Override
            protected void populateViewHolder(StoryViewHolder viewHolder, story_des model, int position) {
                viewHolder.setStoryTitle(model.getStories());
            }
        };
        mStoryList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder{

        View mStoryView;

        public StoryViewHolder(View itemView) {
            super(itemView);

            mStoryView = itemView;
        }

        public void setStoryTitle(String storyTitle){
            TextView story_field = (TextView) mStoryView.findViewById(R.id.story_row);
            story_field.setText(storyTitle);
        }
    }
}
