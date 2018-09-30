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

public class AddNote extends AppCompatActivity {

    private ProgressDialog mNoteProgress;
    private Button newNotesBtn;
    private DatabaseReference newNoteDatabase;
    private EditText newNoteField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mNoteProgress = new ProgressDialog(this);

        newNotesBtn = (Button) findViewById(R.id.newAddNote);

        newNoteField = (EditText) findViewById(R.id.newNote);

        newNoteDatabase = FirebaseDatabase.getInstance().getReference().child("NOTE");

        newNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNoteProgress.setMessage("Note uploading...");
                mNoteProgress.show();
                String note = newNoteField.getText().toString().trim();
                if(!TextUtils.isEmpty(note)) {
                    DatabaseReference newNoteadding = newNoteDatabase.push();
                    newNoteadding.child("Notes").setValue(note);
                    mNoteProgress.dismiss();
                }
                //newNoteDatabase.push().setValue(note);

            }
        });
    }
}
