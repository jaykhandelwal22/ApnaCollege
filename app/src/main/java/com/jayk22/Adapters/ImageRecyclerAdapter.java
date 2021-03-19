package com.jayk22.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayk22.Model.Image;
import com.jayk22.collegeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;
    private OnItemClickListener mListener;


    public ImageRecyclerAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
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

        View view= LayoutInflater.from(context).inflate(R.layout.journal_row_image,parent,false);
        return new ViewHolder(view,context,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecyclerAdapter.ViewHolder holder, int position) {


        Image img=imageList.get(position);
        // holder.constraintLayout.setVisibility(View.INVISIBLE);
        //holder.imgView.setVisibility(View.INVISIBLE);
        holder.mainImg.setVisibility(View.VISIBLE);
        String timeago= (String) DateUtils.getRelativeTimeSpanString(img.getTimestamp().getSeconds()*1000);
        holder.time.setText(""+timeago);
        holder.liketxt.setText(""+img.getLikes());
        holder.commenttxt.setText(""+img.getComments());

           if(holder.mainImg.getDrawable()==null)
           {
               Picasso.get().load(img.getImageUrl())
                       .placeholder(R.drawable.image_three)
                       .fit().into(holder.mainImg);
           }


        int l=img.getLikes();
        if(l>0)
        {
            holder.likesImg.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else
        {
            holder.likesImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mainImg;
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

            mainImg=itemView.findViewById(R.id.journal_image_list);
            // imgView=itemView.findViewById(R.id.journal_image_list);
            // constraintLayout=itemView.findViewById(R.id.poll_layout);
            likesImg=itemView.findViewById(R.id.img_like_btn);
            commentImg=itemView.findViewById(R.id.img_cmnt_btn);
            liketxt=itemView.findViewById(R.id.img_ct_like_txt);
            commenttxt=itemView.findViewById(R.id.img_ct_cmt_txt);
            time=itemView.findViewById(R.id.img_time_ago_txt);
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
