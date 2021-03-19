package com.jayk22.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jayk22.Adapters.CommentRecyclerAdapter;
import com.jayk22.Adapters.PollRecyclerAdapter;
import com.jayk22.Adapters.TextRecyclerAdapter;
import com.jayk22.Model.Comment;
import com.jayk22.Model.Image;
import com.jayk22.Model.Poll;
import com.jayk22.Model.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private List<Comment> myComments;
    private FirebaseFirestore firebaseFirestore;

    SwipeRefreshLayout swipeRefreshLayout;
    CommentRecyclerAdapter commentRecyclerAdapter;
    private DocumentSnapshot lastquerydocument;

    private EditText addComment;
    private TextView postComment;

    String textid;
    String imageid;
    String pollid;
    int flag = 0;
    int fsize=0;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        myRecyclerView = findViewById(R.id.cmt_recyclerView);
        swipeRefreshLayout = findViewById(R.id.cmnt_swipeRefreshLayout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        addComment = findViewById(R.id.add_comment);
        postComment = findViewById(R.id.cmt_post);
        progressBar=findViewById(R.id.cmnt_progress_bar);
        myComments = new ArrayList<>();
        commentRecyclerAdapter = new CommentRecyclerAdapter(getApplicationContext(), myComments);
        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(commentRecyclerAdapter);
        GetTextDocuments();
        GetComments();




        //commentRecyclerAdapter.notifyDataSetChanged();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRecyclerView.setVisibility(View.INVISIBLE);
                //GetTextDocuments();
               // GetComments();
                commentRecyclerAdapter.notifyDataSetChanged();
                myRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddDocuments();
            }
        });


    }

    private void GetTextDocuments() {

        //Intent intent=new Intent();

       flag=getIntent().getIntExtra("flag",-1);

        Log.e("Textcmntid", "" + textid);


    }


    private void GetComments() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");
        CollectionReference collectionReference1 = null;
        if (flag == 0) {
            textid=getIntent().getStringExtra("textid");
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Texts").collection("MyTexts").document(textid).collection("Comments");
        }

        if (flag == 1) {
            pollid=getIntent().getStringExtra("pollid");
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Polls").collection("MyPolls").document(pollid).collection("Comments");


        }

        if (flag == 2) {
            imageid=getIntent().getStringExtra("imageid");
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Images").collection("MyImages").document(imageid).collection("Comments");


        }
        Query query = null;

        if (query != null) {
            query = collectionReference1.orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastquerydocument);
        } else {
            query = collectionReference1.orderBy("timestamp", Query.Direction.DESCENDING);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (!task.getResult().isEmpty()) {

                        for (QueryDocumentSnapshot comments : task.getResult()) {

                            Comment comment = comments.toObject(Comment.class);
                            //comment.setTextId(texts.getId());
                            myComments.add(comment);
                        }

                        fsize=task.getResult().size();

                        if (task.getResult().size() != 0) {
                            lastquerydocument = task.getResult().getDocuments().get(task.getResult().size() - 1);

                        }



                    }

                    commentRecyclerAdapter.notifyDataSetChanged();

                } else {


                }

            }
        });


    }

    private void AddDocuments() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");
        CollectionReference collectionReference1 = null;
        if (flag == 0) {
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Texts").collection("MyTexts").document(textid).collection("Comments");
        }

        if (flag == 1) {
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Polls").collection("MyPolls").document(pollid).collection("Comments");
            ;

        }

        if (flag == 2) {
            collectionReference1 = firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                    .document("Images").collection("MyImages").document(imageid).collection("Comments");
            ;

        }

        Comment comment = new Comment();
        comment.setText(addComment.getText().toString());
        comment.setTimestamp(new Timestamp(new Date()));


        collectionReference1.add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, "Error !" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        myComments.clear();

        GetComments();

       if(flag==0)
       {
           DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                   .document("Texts").collection("MyTexts").document(textid);

           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   DocumentSnapshot tsts=task.getResult();
                   Text text=tsts.toObject(Text.class);
                   documentReference.update("comments",text.getComments()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                       }
                   });


               }
           });
       }
       else if(flag==1)
       {
           DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                   .document("Polls").collection("MyPolls").document(pollid);

           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   DocumentSnapshot tsts=task.getResult();
                   Poll poll=tsts.toObject(Poll.class);
                   documentReference.update("comments",poll.getComments()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                       }
                   });


               }
           });
       }
       else if(flag==2)
       {
           DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                   .document("Images").collection("MyImages").document(imageid);

           documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   DocumentSnapshot tsts=task.getResult();
                   Image image=tsts.toObject(Image.class);
                   documentReference.update("comments",image.getComments()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                       }
                   });


               }
           });
       }

        progressBar.setVisibility(View.GONE);

        //commentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //AddComments();
    }
}

