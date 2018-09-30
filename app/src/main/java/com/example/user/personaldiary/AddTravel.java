package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddTravel extends AppCompatActivity {


    private ImageButton mSelectImage;
    private Button newTravelBtn;
    private DatabaseReference newTravelDatabase;
    private EditText newTravelField;
    private static final int GALLERY_REQUEST = 2;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgressTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        mStorage = FirebaseStorage.getInstance().getReference();

        mProgressTravel = new ProgressDialog(this);

        mSelectImage = (ImageButton) findViewById(R.id.ImageSelect);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        newTravelBtn = (Button) findViewById(R.id.newAddTravel);
        newTravelField = (EditText) findViewById(R.id.newTravelPlan);

        newTravelDatabase = FirebaseDatabase.getInstance().getReference().child("TRAVEL");

        newTravelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTravel();

            }
        });
    }

    private void startTravel(){
        mProgressTravel.setMessage("Adding Travel Plan...");
        mProgressTravel.show();

        final String travel =  newTravelField.getText().toString().trim();

        if(!TextUtils.isEmpty(travel) && mImageUri !=null){
            StorageReference filepath = mStorage.child("Travel_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newTravel = newTravelDatabase.push();
                    newTravel.child("TRAVEL_PLAN").setValue(travel);
                    newTravel.child("image").setValue(mImageUri.toString());


                    mProgressTravel.dismiss();
                }

            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){



            mProgressTravel.setMessage("Uploading...");
            mProgressTravel.show();
            mImageUri = data.getData();
            //mSelectImage.setImageURI(mImageUri);
            mProgressTravel.dismiss();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mSelectImage.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
}
