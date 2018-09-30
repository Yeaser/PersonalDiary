package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PICTURE extends AppCompatActivity {


   // private Button mPictureBtn;
  //  private StorageReference mPictureStorage;
   // private static final int GALLERY_INTENT = 2;
  //  private ProgressDialog mProgressDialog;
  //  private DatabaseReference mRetrieveImage;
  //  private ImageView mShowImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

       // mRetrieveImage = FirebaseDatabase.getInstance().getReference().child("Pic_Url");

     //   mShowImage = (ImageView) findViewById(R.id.Show_Image);


/*

        mRetrieveImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Picasso.with(PICTURE.this).load(value).fit().centerCrop().into(mShowImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/

    }

}
