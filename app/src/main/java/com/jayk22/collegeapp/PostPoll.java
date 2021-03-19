package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jayk22.Model.Poll;
import com.jayk22.Model.Text;

import java.util.Date;

public class PostPoll extends AppCompatActivity {

    private TextView goBackButton;
    private EditText pollQuestion;
    private EditText pollOp1;
    private EditText pollOp2;
    private EditText pollOp3;
    private EditText pollOp4;
    private Button pollSubmit;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_poll);

        goBackButton=findViewById(R.id.poll_go_back_txt);
        pollQuestion=findViewById(R.id.poll_quest_edittxt);
        pollOp1=findViewById(R.id.op1_ed_txt);
        pollOp2=findViewById(R.id.op2_ed_txt);
        pollOp3=findViewById(R.id.op3_ed_txt);
        pollOp4=findViewById(R.id.op4_ed_txt);
        pollSubmit=findViewById(R.id.poll_post_btn);
        progressBar=findViewById(R.id.poll_prg_bar);
        firebaseFirestore=FirebaseFirestore.getInstance();

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostPoll.this,HomeActivity.class));
                finish();
            }
        });

        pollSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPoll();
            }
        });

    }

    private void PostPoll()
    {
        String q=pollQuestion.getText().toString();
        String op1=pollOp1.getText().toString();
        String op2=pollOp2.getText().toString();
        String op3=pollOp3.getText().toString();
        String op4=pollOp4.getText().toString();

        if(TextUtils.isEmpty(q))
        {
            Toast.makeText(this, "Enter valid question", Toast.LENGTH_SHORT).show();
            return;
        }
        int cEmpty=0;
        if(TextUtils.isEmpty(op1))
        {
            cEmpty++;
        }
        if(TextUtils.isEmpty(op2))
        {
            cEmpty++;
        }
        if(TextUtils.isEmpty(op3)){ cEmpty++;}

        if(TextUtils.isEmpty(op4)) {cEmpty++;}

        if(cEmpty>2)
        {
            Toast.makeText(this, "Enter atleast 2 options", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(op1) && TextUtils.isEmpty(op2)&& TextUtils.isEmpty(op3)&& TextUtils.isEmpty(op4))
        {
            Toast.makeText(this, "Enter atleast 2 options", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");
        String currentUserID=saved_values.getString(RegisterActivity.CURRENT_USERID,"");

        Poll poll=new Poll();

        poll.setComments(0);
        poll.setLikes(0);
        poll.setOp1(op1);
        poll.setOp2(op2);
        poll.setOp3(op3);
        poll.setOp4(op4);
        poll.setQuestion(q);
        poll.setTimestamp(new Timestamp(new Date()));
        poll.setOp1v(0);
        poll.setOp2v(0);
        poll.setOp3v(0);
        poll.setOp4v(0);

        UploadData(poll,collegeEmail,currentUserID);



    }

    private void UploadData(Poll poll,String collegeEmail,String currentUserID)
    {
        CollectionReference collectionReference=firebaseFirestore.collection(collegeEmail)
                .document("users")
                .collection("AllUsers").document(currentUserID).collection("MyPosts")
                .document("Poll").collection("MyPolls");

        collectionReference.add(poll).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                poll.setPollid(id);

                collectionReference.document(id).update("pollid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostPoll.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Polls").collection("MyPolls");


        collectionReference1.add(poll).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                poll.setPollid(id);

                collectionReference1.document(id).update("pollid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
/*
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostPoll.this,"Poll Created Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostPoll.this,HomeActivity.class));
                finish();*/

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostPoll.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        CollectionReference collectionReference2=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("AllPost").collection("EveryPost");


        collectionReference2.add(poll).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                String id=documentReference.getId();
                poll.setPollid(id);

                collectionReference2.document(id).update("pollid",id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(PostPoll.this, "Success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostPoll.this,HomeActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostPoll.this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}