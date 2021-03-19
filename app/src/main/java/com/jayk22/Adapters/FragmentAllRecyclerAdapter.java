package com.jayk22.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayk22.Model.Image;
import com.jayk22.Model.Poll;
import com.jayk22.Model.Text;

import java.util.List;

public class FragmentAllRecyclerAdapter extends RecyclerView.Adapter<FragmentAllRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Image> imageList;
    private List<Text> textList;
    private List<Poll> pollList;



    @NonNull
    @Override
    public FragmentAllRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentAllRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
