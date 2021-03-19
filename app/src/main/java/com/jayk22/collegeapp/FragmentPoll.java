package com.jayk22.collegeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jayk22.Adapters.PollRecyclerAdapter;
import com.jayk22.Adapters.TextRecyclerAdapter;
import com.jayk22.Model.Poll;
import com.jayk22.Model.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentPoll extends Fragment {


    View v;
    private RecyclerView myRecyclerView;
    private List<Poll> myPolls;
    private FirebaseFirestore firebaseFirestore;

    SwipeRefreshLayout swipeRefreshLayout;
    PollRecyclerAdapter pollRecyclerAdapter;
    private DocumentSnapshot lastquerydocument;

    public FragmentPoll() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.poll_fragment,container,false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();
        myPolls=new ArrayList<>();

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



        pollRecyclerAdapter=new PollRecyclerAdapter(getContext(),myPolls);

        //myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //textRecyclerAdapter.notifyDataSetChanged();
        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(pollRecyclerAdapter);
        pollRecyclerAdapter.notifyDataSetChanged();

        pollRecyclerAdapter.setOnItemClickListener(new PollRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {

            }

            @Override
            public void opClicked(int position, String op) {

                Log.e("FragmentPollll", "opClicked: "+"Clicked fragmet polll");
                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(FragmentPoll.this.getContext());
                String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");

                DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                        .document("Polls").collection("MyPolls").document(myPolls.get(position).getPollid());

                Poll pc=myPolls.get(position);

                int votes=0;
                if(op=="op1v")
                {
                    votes=pc.getOp1v()+1;
                    myPolls.get(position).setOp1v(votes);
                }
                else if(op=="op2v")
                {
                    votes=pc.getOp2v()+1;
                    myPolls.get(position).setOp2v(votes);
                }
                else if(op=="op3v")
                {
                    votes=pc.getOp3v()+1;
                    myPolls.get(position).setOp3v(votes);
                }
                else
                {
                    votes=pc.getOp4v()+1;
                    myPolls.get(position).setOp4v(votes);
                }

                if(!pc.isSeen())
                {
                    myPolls.get(position).setSeen(true);
                    pollRecyclerAdapter.notifyDataSetChanged();
                    documentReference.update(op,votes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    documentReference.update("seen",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }

            @Override
            public void onLikeImgClick(int position,boolean clicked)
            {
                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(FragmentPoll.this.getContext());
                String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");

                DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                        .document("Polls").collection("MyPolls").document(myPolls.get(position).getPollid());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot tsts=task.getResult();
                        Poll poll=tsts.toObject(Poll.class);
                        int like=poll.getLikes();
                        if(clicked==true)
                        {

                            if(poll.isUserliked())
                            {
                                like--;
                                if(like<0)
                                {
                                    like=0;
                                }
                                poll.setUserliked(false);

                            }
                        }
                        else
                        {
                            if(!poll.isUserliked())
                            {
                                like++;
                                poll.setUserliked(true);
                            }



                        }

                        myPolls.get(position).setLikes(like);
                        myPolls.get(position).setUserliked(poll.isUserliked());
                        pollRecyclerAdapter.notifyDataSetChanged();
                        documentReference.update("likes",like).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {





                            }
                        });

                        documentReference.update("userliked",poll.isUserliked()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                Toast.makeText(FragmentPoll.this.getContext(), myPolls.get(position).getPollid(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FragmentPoll.this.getActivity(),CommentActivity.class);
                intent.putExtra("pollid",myPolls.get(position).getPollid());
                intent.putExtra("flag",1);

                startActivity(intent);



            }
        });


    }

    private void GetTextDocuments()
    {
        //progressBar.setVisibility(View.VISIBLE);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");

        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Polls").collection("MyPolls");

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
                    for (QueryDocumentSnapshot polls:task.getResult())
                    {
                        Poll poll=polls.toObject(Poll.class);
                        myPolls.add(poll);
                    }

                    if(task.getResult().size()!=0)
                    {
                        lastquerydocument=task.getResult().getDocuments().get(task.getResult().size()-1);

                    }

                    pollRecyclerAdapter.notifyDataSetChanged();

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
                //  GetTextDocuments();
                pollRecyclerAdapter.notifyDataSetChanged();
                myRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }


}
