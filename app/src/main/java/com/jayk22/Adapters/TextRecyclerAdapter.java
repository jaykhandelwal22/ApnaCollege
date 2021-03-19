package com.jayk22.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jayk22.Model.Text;
import com.jayk22.collegeapp.R;

import java.util.List;

public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Text> textList;
    private OnItemClickListener mListener;


    public TextRecyclerAdapter(Context context, List<Text> textList) {
        this.context = context;
        this.textList = textList;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public interface OnItemClickListener
    {
        void onItemCLick(int position);
        void onLikeImgClick(int position,boolean clicked);
        void onCmntImgClick(int position);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false);
        return new ViewHolder(view,context,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Text txt=textList.get(position);
       // holder.constraintLayout.setVisibility(View.INVISIBLE);
        //holder.imgView.setVisibility(View.INVISIBLE);
        holder.txtMessage.setVisibility(View.VISIBLE);
        String timeago= (String) DateUtils.getRelativeTimeSpanString(txt.getTimestamp().getSeconds()*1000);
        holder.time.setText(timeago);
        holder.liketxt.setText(""+txt.getLikes());
        holder.commenttxt.setText(""+txt.getComments());
        holder.txtMessage.setText(txt.getText());
        int l=txt.getLikes();
        if(l>0)
        {
            holder.likesImg.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else
        {
            holder.likesImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

       // holder.progressBar.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return textList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtMessage;
      //  private ImageView imgView;
       // private ConstraintLayout constraintLayout;
        private ImageView likesImg,commentImg;
        private TextView liketxt,commenttxt;
        private TextView time;
        boolean clicked=true;
       // private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView, Context ctx,OnItemClickListener listener) {
            super(itemView);
            context=ctx;

            txtMessage=itemView.findViewById(R.id.txt_message_txt);
           // imgView=itemView.findViewById(R.id.journal_image_list);
           // constraintLayout=itemView.findViewById(R.id.poll_layout);
            likesImg=itemView.findViewById(R.id.like_btn);
            commentImg=itemView.findViewById(R.id.cmnt_btn);
            liketxt=itemView.findViewById(R.id.ct_like_txt);
            commenttxt=itemView.findViewById(R.id.ct_cmt_txt);
            time=itemView.findViewById(R.id.time_ago_txt);
            //progressBar=itemView.findViewById(R.id.txt_progressbar);



            commentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onCmntImgClick(position);
                        }
                    }
                }
            });
            likesImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked=!clicked;

                       if(clicked==false)
                       {
                           likesImg.setImageResource(R.drawable.ic_baseline_favorite_24);
                       }
                       if(clicked==true)
                       {
                           likesImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                       }

                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onLikeImgClick(position,clicked);
                        }
                    }


                }
            });
        }
    }
}
