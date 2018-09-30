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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText loginEmailField;
    private EditText loginPasswordField;
    private Button loginButtonField;
    private Button createAccountField;

    private ProgressDialog loginProgress;

    private FirebaseAuth loginAuth;
    private DatabaseReference loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAuth = FirebaseAuth.getInstance();
        loginDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        loginDatabase.keepSynced(true);

        loginProgress = new ProgressDialog(this);

        loginEmailField = (EditText) findViewById(R.id.loginEmail);
        loginPasswordField = (EditText) findViewById(R.id.loginPassword);
        loginButtonField = (Button) findViewById(R.id.loginButton);
        createAccountField = (Button) findViewById(R.id.createAccountBtn);

        createAccountField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_Intent = new Intent(LoginActivity.this, RegisterActivity.class);
                reg_Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(reg_Intent);
            }
        });

        loginButtonField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });


    }

    private void checkLogin() {

        String login_email = loginEmailField.getText().toString().trim();
        String login_Pass = loginPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(login_email) && !TextUtils.isEmpty(login_Pass)){

            loginProgress.setMessage("Logging in....");
            loginProgress.setMessage("Please wait while we check your credentials.");
            loginProgress.setCanceledOnTouchOutside(false);
            loginProgress.show();

            loginAuth.signInWithEmailAndPassword(login_email, login_Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        loginProgress.dismiss();
                        checkUserExist();

                    }
                    else{
                        loginProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }

    private void checkUserExist() {

        final String user_id = loginAuth.getCurrentUser().getUid();
        loginDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                }
                else {
                    Intent register_Intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    register_Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //register_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(register_Intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
