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
import com.jayk22.Adapters.ImageRecyclerAdapter;
import com.jayk22.Adapters.TextRecyclerAdapter;
import com.jayk22.Model.Image;
import com.jayk22.Model.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentImage extends Fragment {
    View v;
    private RecyclerView myRecyclerView;
    private List<Image> myImages;
    private FirebaseFirestore firebaseFirestore;

    SwipeRefreshLayout swipeRefreshLayout;
    ImageRecyclerAdapter imageRecyclerAdapter;
    private DocumentSnapshot lastquerydocument;

    public FragmentImage() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.image_fragment,container,false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();
        myImages=new ArrayList<>();

        GetImageDocuments();

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



        imageRecyclerAdapter=new ImageRecyclerAdapter(getContext(),myImages);

        //myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //textRecyclerAdapter.notifyDataSetChanged();
        myRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(imageRecyclerAdapter);
        imageRecyclerAdapter.notifyDataSetChanged();

        imageRecyclerAdapter.setOnItemClickListener(new ImageRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {

            }

            @Override
            public void onLikeImgClick(int position,boolean clicked)
            {
                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(FragmentImage.this.getContext());
                String collegeEmail = saved_values.getString(RegisterActivity.COLLEGE_EMAIL, "");

                DocumentReference documentReference= firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                        .document("Images").collection("MyImages").document(myImages.get(position).getImageid());

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot tsts=task.getResult();
                        Image image=tsts.toObject(Image.class);
                        int like=image.getLikes();
                        if(clicked==true)
                        {

                            if(image.isUserliked())
                            {
                                like--;
                                if(like<0)
                                {
                                    like=0;
                                }
                                image.setUserliked(false);

                            }
                        }
                        else
                        {
                            if(!image.isUserliked())
                            {
                                like++;
                                image.setUserliked(true);
                            }



                        }

                        myImages.get(position).setLikes(like);
                        myImages.get(position).setUserliked(image.isUserliked());
                        imageRecyclerAdapter.notifyDataSetChanged();
                        documentReference.update("likes",like).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {





                            }
                        });

                        documentReference.update("userliked",image.isUserliked()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                Toast.makeText(FragmentImage.this.getContext(), myImages.get(position).getImageid(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FragmentImage.this.getActivity(),CommentActivity.class);
                intent.putExtra("imageid",myImages.get(position).getImageid());
                intent.putExtra("flag",2);

                startActivity(intent);



            }
        });

    }


    private void GetImageDocuments() {

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        String collegeEmail=saved_values.getString(RegisterActivity.COLLEGE_EMAIL,"");

        CollectionReference collectionReference1=firebaseFirestore.collection(collegeEmail).document("AllPosts").collection("MyPosts")
                .document("Images").collection("MyImages");

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
                    for (QueryDocumentSnapshot images:task.getResult())
                    {
                        Image img=images.toObject(Image.class);
                        myImages.add(img);
                    }

                    if(task.getResult().size()!=0)
                    {
                        lastquerydocument=task.getResult().getDocuments().get(task.getResult().size()-1);

                    }

                    imageRecyclerAdapter.notifyDataSetChanged();

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
                imageRecyclerAdapter.notifyDataSetChanged();
                myRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
