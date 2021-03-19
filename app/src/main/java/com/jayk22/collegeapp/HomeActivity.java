package com.jayk22.collegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    FloatingActionButton textPostBtn;
    FloatingActionButton PollPostBtn;
    FloatingActionButton ImagePostBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout=findViewById(R.id.tablayout_id);
        viewPager=findViewById(R.id.viewpager_id);
        textPostBtn=findViewById(R.id.text_post_btn);
        PollPostBtn=findViewById(R.id.poll_post_btn);
        ImagePostBtn=findViewById(R.id.image_post_btn);
        adapter=new ViewPagerAdapter(getSupportFragmentManager());



       // adapter.AddFragment(new FragmentAll(),"All");
        adapter.AddFragment(new FragmentText(),"Text");
        adapter.AddFragment(new FragmentPoll(),"Poll");
        adapter.AddFragment(new FragmentImage(),"Image");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_view_list_24);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_text_snippet_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_poll_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_image_24);

        textPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,PostText.class));
            }
        });

        PollPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,PostPoll.class));

            }
        });

        ImagePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,PostImage.class));

            }
        });







    }

    public void LogOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this,MainActivity.class));
        finish();
    }
}