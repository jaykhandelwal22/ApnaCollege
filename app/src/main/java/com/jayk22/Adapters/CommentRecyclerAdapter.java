package com.jayk22.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayk22.Model.Comment;
import com.jayk22.collegeapp.R;

import java.util.List;


public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commmentList;

    public CommentRecyclerAdapter(Context context, List<Comment> commmentList) {
        this.context = context;
        this.commmentList = commmentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.journal_cmnt,parent,false);
        return new CommentRecyclerAdapter.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.ViewHolder holder, int position)
    {
        Comment comment=commmentList.get(position);
        String timeago= (String) DateUtils.getRelativeTimeSpanString(comment.getTimestamp().getSeconds()*1000);
        holder.cmntTime.setText(""+timeago);
        holder.textCmnt.setText(comment.getText());

    }

    @Override
    public int getItemCount() {
        return commmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textCmnt;
        private TextView cmntTime;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            textCmnt=itemView.findViewById(R.id.cmt_message_txt);
            cmntTime=itemView.findViewById(R.id.cmt_time_ago_txt);
        }
    }


}
