package com.jayk22.collegeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jayk22.Adapters.PollRecyclerAdapter;
import com.jayk22.Adapters.TextRecyclerAdapter;
import com.jayk22.Model.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentText extends Fragment {

    View v;
    private RecyclerView myRecyclerView;
    private List<Text> myTexts;
    private FirebaseFirestore firebaseFirestore;

    SwipeRefreshLayout swipeRefreshLayout;
    TextRecyclerAdapter textRecyclerAdapter;
    private DocumentSnapshot lastquerydocument;



    public FragmentText() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       v=inflater.inflate(R.layout.text_fragment,container,false);





       // progressBar=container.findViewById(R.id.text_prg_bar);


       return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();
        myTexts=new ArrayList<>();

        GetTextDocuments();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button even
                Log.d("BACKBUTTON", "Back button clicks");
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myRecyclerView=view.findViewById(R.id.txt_recyclerView);
        swipeRefreshLayout=v.findViewById(R.id.swipeRefreshLayout);



        textRecyclerAdapter=new TextRecyclerAdapter(getContext(),myTexts);

        //myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //textRecyclerAdapter.notifyDataSetChanged();
        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(textRecyclerAdapter);
        textRecyclerAdapter.notifyDataSetChanged();

        textRecyclerAdapter.setOnItemClickListener(new TextRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {

            }

            @Override
            public void onLikeImgClick(int position,boolean clicked)
            {
                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(FragmentText.this.getContext());
                String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");

                DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                        .document("Texts").collection("MyTexts").document(myTexts.get(position).getTextId());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot tsts=task.getResult();
                        Text text=tsts.toObject(Text.class);
                        int like=text.getLikes();
                        if(clicked==true)
                        {

                            if(text.isUserliked())
                            {
                                like--;
                                if(like<0)
                                {
                                    like=0;
                                }
                                text.setUserliked(false);

                            }
                        }
                        else
                        {
                            if(!text.isUserliked())
                            {
                                like++;
                                text.setUserliked(true);
                            }



                        }

                        myTexts.get(position).setLikes(like);
                        myTexts.get(position).setUserliked(text.isUserliked());
                        textRecyclerAdapter.notifyDataSetChanged();
                        documentReference.update("likes",like).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {





                            }
                        });

                        documentReference.update("userliked",text.isUserliked()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {





                            }
                        });


                    }
                });


            }
            @Override
            public void onCmntImgClick(int position)
            {
                Toast.makeText(FragmentText.this.getContext(), myTexts.get(position).getTextId(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FragmentText.this.getActivity(),CommentActivity.class);
                intent.putExtra("textid",myTexts.get(position).getTextId());
                intent.putExtra("flag",0);

                startActivity(intent);



            }
        });


    }

    private void GetTextDocuments()
    {

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");

        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Texts").collection("MyTexts");

        Query query=null;

        if(query!=null)
        {
            query=collectionReference1.orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastquerydocument);
        }
        else {
            query=collectionReference1.orderBy("timestamp", Query.Direction.DESCENDING);
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot texts:task.getResult())
                    {

                        Text text=texts.toObject(Text.class);
                        text.setTextId(texts.getId());
                        myTexts.add(text);
                    }

                    if(task.getResult().size()!=0)
                    {
                        lastquerydocument=task.getResult().getDocuments().get(task.getResult().size()-1);

                    }

                    textRecyclerAdapter.notifyDataSetChanged();

                }
                else
                {


                }

            }
        });





    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRecyclerView.setVisibility(View.INVISIBLE);
                textRecyclerAdapter.notifyDataSetChanged();
                myRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

}
