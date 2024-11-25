package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_wardenprofile extends AppCompatActivity {

//    TextView sFname, sLname, sEmail;
    TextView sFname, sLname, sEmail;
    CircleImageView profileImageView;
    TextInputEditText displayNameEditText;
    Button updateProfileButton;
    ProgressBar progressBar;
    Uri imageUri;
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardenprofile);

        sFname = findViewById(R.id.firstName);
        profileImageView = findViewById(R.id.profile_image2);
        sLname = findViewById(R.id.lastName);
        sEmail= findViewById(R.id.emailID);

        FirebaseUser sUser = FirebaseAuth.getInstance().getCurrentUser();
        String studentEmail = sUser.getEmail();
        sEmail.setText(studentEmail);

        String userID = sUser.getUid();

        if (sUser != null) {

            if (sUser.getPhotoUrl() != null) {
                Picasso.with(this)
                        .load(sUser.getPhotoUrl()).placeholder(R.mipmap.ic_launcher_round)
                        .into(profileImageView);
            }
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
                //the image is selected successfully from the gallery
                //now we need to upload the image in the firebase storage.
            }
        });





        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("userID").child(userID);
        mFirebaseDatabase.child("First name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                sFname.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        mFirebaseDatabase.child("Last name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                sLname.setText(String.valueOf(task.getResult().getValue()));
            }
        });
    }

    public void handleUpload1(View v) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading...");

        dialog.show();
        if(bitmap==null){
            Toast.makeText(this,"Some Error Occured Pleasy Try Again",Toast.LENGTH_SHORT).show();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference,dialog);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e.getCause() );
                        dialog.dismiss();

                    }
                });
    }

    private void getDownloadUrl(StorageReference reference,ProgressDialog dialog) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri,dialog);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri,ProgressDialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity_wardenprofile.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_wardenprofile.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
    }
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    //this result is the result of uri
                    if (result != null) {
                        profileImageView.setImageURI(result);
//                        image will be set in the imageUri
                        imageUri=result;

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver() , result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                    profileImageView.setImageBitmap(bitmap);
//                    handleUpload(bitmap);
                    }
                }
            });
}