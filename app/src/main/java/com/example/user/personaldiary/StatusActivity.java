package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private ProgressDialog mStatusProg;
    private Button mSavebtn;
    private EditText mStatus;
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mStatusCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mStatusCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_UserId = mStatusCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_UserId);

        String status_val = getIntent().getStringExtra("status_value");


        mStatus = (EditText) findViewById(R.id.update_status_field);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);

        mStatus.setText(status_val);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusProg = new ProgressDialog(StatusActivity.this);
                mStatusProg.setTitle("Saving Changes");
                mStatusProg.setMessage("Please wait while we save changes....");
                mStatusProg.show();
                String status = mStatus.getText().toString().trim();
                mStatusDatabase.child("Status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            mStatusProg.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "THERE WAS SOME ERROR WHILE SAVING CHANGES." , Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

    }
}
