package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStory extends AppCompatActivity {

    private ProgressDialog mPrgStory;
    private Button newStoryBtn;
    private DatabaseReference newStoryDatabase;
    private EditText newStoryField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);


        mPrgStory = new ProgressDialog(this);

        newStoryBtn = (Button) findViewById(R.id.newAddStory);

        newStoryField = (EditText) findViewById(R.id.newStory);

        newStoryDatabase = FirebaseDatabase.getInstance().getReference().child("STORY");

        newStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPrgStory.setMessage("Adding Story...");
                mPrgStory.show();

                    String story = newStoryField.getText().toString().trim();
                    if (!TextUtils.isEmpty(story)) {
                        DatabaseReference newStoryAdding = newStoryDatabase.push();
                        newStoryAdding.child("Stories").setValue(story);
                        mPrgStory.dismiss();
                    }
                //newStoryDatabase.push().setValue(story);

            }
        });

    }
}
