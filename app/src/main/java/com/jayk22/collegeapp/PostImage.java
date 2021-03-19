package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jayk22.Model.Image;

import java.util.Date;

public class PostImage extends AppCompatActivity {

    private static final int GALLERY_CODE =1 ;
    private TextView goBackButton;

    private Button pstImage;

    private ImageView addPhotoBtn;
    private EditText title;
    private EditText comment;

    private ProgressBar progressBar;
    private Uri imageuri;

    private ImageView imageView;

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    private Button imgPostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        goBackButton=findViewById(R.id.img_goback_btn);
        pstImage=findViewById(R.id.img_btn_post);
        title=findViewById(R.id.img_title_edittxt);
        comment=findViewById(R.id.img_cmt_edittxt);
        addPhotoBtn=findViewById(R.id.post_cmra_img_btn);
        imageView=findViewById(R.id.imageView2);
        progressBar=findViewById(R.id.img_progressBar);
        imgPostBtn=findViewById(R.id.img_btn_post);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostImage.this,HomeActivity.class));
                finish();
            }
        });


        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        imgPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostImage.this, "imgbtnpress", Toast.LENGTH_SHORT).show();
                UploadPicture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                imageuri=data.getData();
                imageView.setImageURI(imageuri);

            }
            else
            {
                Toast.makeText(this, "data null", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void UploadPicture()
    {

        String titletxt=title.getText().toString();
        String commenttxt=comment.getText().toString();

        if(imageuri==null)
        {
            Toast.makeText(this, "Upload Valid Image", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");
        String currentUserID=saved_values.getString(RegisterActivity.CURRENT_USERID,"");

        StorageReference filePath=storageReference.child(collegeEmail).child("myImage"+ Timestamp.now().getSeconds());

        filePath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Image image=new Image();
                        image.setImageUrl(uri.toString());
                        image.setTitle(titletxt);
                        image.setLikes(0);
                        image.setComments(0);
                        image.setTimestamp(new Timestamp(new Date()));

                        PostImage(image,collegeEmail,currentUserID);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    private void PostImage(Image image,String collegeEmail,String currentUserID)
    {

        CollectionReference collectionReference=firebaseFirestore.collection(collegeEmail)
                .document("users")
                .collection("AllUsers").document(currentUserID).collection("MyPosts")
                .document("Image").collection("MyImages");

        collectionReference.add(image).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                image.setImageid(id);

                collectionReference.document(id).update("imageid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostImage.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Images").collection("MyImages");


        collectionReference1.add(image).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                image.setImageid(id);

                collectionReference1.document(id).update("imageid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

               /* progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostImage.this,"Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostImage.this,HomeActivity.class));
                finish();*/

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostImage.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        CollectionReference collectionReference2=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("AllPost").collection("EveryPost");


        collectionReference2.add(image).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                image.setImageid(id);

                collectionReference2.document(id).update("imageid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostImage.this, "Success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostImage.this,HomeActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostImage.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}