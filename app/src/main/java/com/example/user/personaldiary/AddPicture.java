package com.example.user.personaldiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPicture extends AppCompatActivity {

    private Button newPictureBtn;
    private StorageReference newPictureStorage;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog newProgressDialog;
    private DatabaseReference mPictureUrlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        newPictureBtn = (Button) findViewById(R.id.newAddPicture);

        newPictureStorage = FirebaseStorage.getInstance().getReference();

        newProgressDialog = new ProgressDialog(this);

        mPictureUrlDatabase = FirebaseDatabase.getInstance().getReference().child("Pic_Url");

        newPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            newProgressDialog.setMessage("Uploading....");
            newProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = newPictureStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    newProgressDialog.dismiss();

                    Uri getdownloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPic = mPictureUrlDatabase.push();
                    newPic.child("image").setValue(getdownloadUrl.toString());

                    Toast.makeText(AddPicture.this, "Upload Done", Toast.LENGTH_LONG).show();

                }
            });
        }

    }

}
