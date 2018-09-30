package com.example.user.personaldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Note extends AppCompatActivity {

    private RecyclerView mNoteList;
    private Button mNotesBtn;
    private DatabaseReference mNoteDatabase;
    //private EditText mNoteField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mNoteDatabase = FirebaseDatabase.getInstance().getReference().child("NOTE");

        mNoteList =(RecyclerView) findViewById(R.id.note_list);
        mNoteList.setHasFixedSize(true);
        mNoteList.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<note_des, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<note_des, NoteViewHolder>(

                note_des.class,
                R.layout.note_row,
                NoteViewHolder.class,
                mNoteDatabase

        ) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, note_des model, int position) {
                      viewHolder.setNoteTitle(model.getNotes());
            }
        };
        mNoteList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{

        View mNoteView;
        public NoteViewHolder(View itemView) {
            super(itemView);

            mNoteView = itemView;

        }
        public void setNoteTitle(String noteTitle){
            TextView note_field = (TextView) mNoteView.findViewById(R.id.note_row);
            note_field.setText(noteTitle);
        }
    }


}
