package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {


    //private static final int MAX_LENGTH = 10;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    //ANDROID LAYOUT

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusButton;
    private Button mImageUpdaterButton;
    private static final int GALLERY_PICK = 1;
    private StorageReference mProfileImage;
    private ProgressDialog mProgressImageUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView) findViewById(R.id.circleImageView);
        mName = (TextView) findViewById(R.id.settings_display_name);
        mStatus = (TextView) findViewById(R.id.settings_status);


        mProfileImage = FirebaseStorage.getInstance().getReference();

        mStatusButton = (Button) findViewById(R.id.settings_status_btn);
        mImageUpdaterButton = (Button) findViewById(R.id.settings_image_btn);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_Uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("Name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();

                //=============================================== new =================================================================

                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                //=============================================== end =================================================================


                mName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.ic_account_circle_black_24dp).into(mDisplayImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mStatus.getText().toString();
                Intent statusIntent = new Intent(SettingsActivity.this, StatusActivity.class);
                statusIntent.putExtra("status_value", status_value);

                startActivity(statusIntent);
            }
        });


        mImageUpdaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updateFROMgallery = new Intent();
                updateFROMgallery.setType("image/*");
                updateFROMgallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(updateFROMgallery, "SELECT IMAGE"),GALLERY_PICK);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageURI = data.getData();

            CropImage.activity(imageURI)
                    .setAspectRatio(1,1)
                    .start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressImageUp = new ProgressDialog(SettingsActivity.this);
                mProgressImageUp.setTitle("UPLOADING IMAGE");
                mProgressImageUp.setMessage("Please wait while uploading image....");
                mProgressImageUp.setCanceledOnTouchOutside(false);
                mProgressImageUp.show();


                Uri resultUri = result.getUri();


                //=============================================== new =================================================================

                File thumb_file_path = new File(resultUri.getPath());

                //=============================================== end =================================================================

                String current_user_ID = mCurrentUser.getUid();

//=============================================== new =================================================================
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_file_path);

                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                } catch (IOException e) {
                    e.printStackTrace();
                }




                final byte[] thumb_byte = baos.toByteArray();

//=============================================== end =================================================================
                StorageReference filepath_image = mProfileImage.child("profile_images").child(current_user_ID + ".jpg");


                //=============================================== new =================================================================

                final StorageReference thumb_filepath_storage = mProfileImage.child("profile_images").child("thumbs").child(current_user_ID + ".jpg");

                //=============================================== end =================================================================

                filepath_image.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){


                            final String download_url = task.getResult().getDownloadUrl().toString();

                            //=============================================== new =================================================================

                            UploadTask uploadTask = thumb_filepath_storage.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadURL = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()) {

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadURL);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                mProgressImageUp.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Success Uploading into Database", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }else{

                                        Toast.makeText(SettingsActivity.this, "ERROR OCCURRED WHILE UPLOADING THUMBNAIL IMAGE", Toast.LENGTH_LONG).show();
                                        mProgressImageUp.dismiss();
                                    }
                                }
                            });


                            //=============================================== end =================================================================




                        }else{

                            Toast.makeText(SettingsActivity.this, "ERROR OCCURRED WHILE UPLOADING IMAGE", Toast.LENGTH_LONG).show();
                            mProgressImageUp.dismiss();
                        }

                    }
                });
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }


    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
