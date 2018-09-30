package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mRegNameField;
    private EditText mRegEmailField;
    private EditText mRegPasswordField;

    private Button mRegBtn;

    private FirebaseAuth mRegAuth;
    private DatabaseReference mRegDatabase;

    private ProgressDialog mRegProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegAuth = FirebaseAuth.getInstance();
        mRegDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRegProgress = new ProgressDialog(this);

        mRegNameField = (EditText) findViewById(R.id.mRegName);
        mRegEmailField = (EditText) findViewById(R.id.mRegEmail);
        mRegPasswordField = (EditText) findViewById(R.id.mRegPassword);
        mRegBtn = (Button) findViewById(R.id.mRegButton);


        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });


    }

    private void startRegister() {

        final String name = mRegNameField.getText().toString().trim();
        String email = mRegEmailField.getText().toString().trim();
        String password = mRegPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mRegProgress.setMessage("Signing Up.....");
            mRegProgress.show();

            mRegAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mRegAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_db = mRegDatabase.child(user_id);
                        current_user_db.child("Name").setValue(name);
                        current_user_db.child("Status").setValue("Hey! I am here.");
                        current_user_db.child("image").setValue("default");


                        //===================================== new =============================================

                        current_user_db.child("thumb_image").setValue("default");

                        //===================================== end =============================================


                        mRegProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        //====================
                        finish();
                    }
                }
            });

        }
    }
}
