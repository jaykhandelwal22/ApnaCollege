package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jayk22.Model.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostText extends AppCompatActivity {
private TextView goBackButton;
private ProgressBar progressBar;
private Button postTxtBtn;
private EditText myText;
private FirebaseAuth firebaseAuth;
private FirebaseUser firebaseUser;
private FirebaseFirestore firebaseFirestore;

private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_text);

        goBackButton=findViewById(R.id.txt_go_back_txt);
        progressBar=findViewById(R.id.text_prg_bar);
        postTxtBtn=findViewById(R.id.text_post_btn);
        myText=findViewById(R.id.enter_txt_edttxt);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        myContext=this;


        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostText.this,HomeActivity.class));
                finish();
            }
        });

        postTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PostText();
            }
        });

    }

    private void PostText()
    {
        progressBar.setVisibility(View.VISIBLE);
        String text=myText.getText().toString();

        if(TextUtils.isEmpty(text))
        {
            Toast.makeText(this, "Enter Valid Text ;)", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseUser=firebaseAuth.getCurrentUser();

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");


        //Toast.makeText(myContext,"hi"+collegeEmail , Toast.LENGTH_SHORT).show();

       // Log.e("collegeEmail", collegeEEmail);

        CollectionReference collectionReference=firebaseFirestore.collection(collegeEmail).document("users")
                .collection("AllUsers").document(firebaseUser.getUid()).collection("MyPosts")
                .document("Texts").collection("MyTexts");

        Text userTxt=new Text();
        userTxt.setComments(0);
        userTxt.setLikes(0);
        userTxt.setText(text);
        userTxt.setTextId(null);
       // Date currentTime = Calendar.getInstance().getTime();
        userTxt.setTimestamp(new Timestamp(new Date()));

        collectionReference.add(userTxt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                userTxt.setTextId(id);

                collectionReference.document(id).update("textId",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myContext, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Texts").collection("MyTexts");


        collectionReference1.add(userTxt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                userTxt.setTextId(id);

                collectionReference1.document(id).update("textId",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myContext, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        CollectionReference collectionReference2=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("AllPost").collection("EveryPost");


        collectionReference2.add(userTxt).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                userTxt.setTextId(id);

                collectionReference2.document(id).update("textId",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(myContext, "Success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostText.this,HomeActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myContext, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}