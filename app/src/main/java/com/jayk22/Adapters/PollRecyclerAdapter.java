package com.jayk22.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayk22.Model.Image;
import com.jayk22.Model.Poll;
import com.jayk22.collegeapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PollRecyclerAdapter extends RecyclerView.Adapter<PollRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Poll> pollList;
    private boolean flag1=true,flag2=true,flag3=true,flag4=true;
    Poll poll;
    private OnItemClickListener mListener;
  //  boolean pollClicked=false;

    public PollRecyclerAdapter(Context context, List<Poll> pollList) {
        this.context = context;
        this.pollList = pollList;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    public interface OnItemClickListener
    {
        void onItemCLick(int position);
        void opClicked(int position,String op);
        void onLikeImgClick(int position,boolean clicked);
        void onCmntImgClick(int position);

    }

    @NonNull
    @Override
    public PollRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.journal_row_poll,parent,false);
        return new PollRecyclerAdapter.ViewHolder(view,context,mListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PollRecyclerAdapter.ViewHolder holder, int position) {

        poll=pollList.get(position);
        String op1=poll.getOp1();
        String op2=poll.getOp2();
        String op3=poll.getOp3();
        String op4=poll.getOp4();

        holder.mainquestion.setText(poll.getQuestion());
       if(!TextUtils.isEmpty(op1))
       {
           holder.op1.setText(op1);
       }
       else
       {
           //holder.op1.touch
           holder.op1.setEnabled(false);
           holder.op1.setVisibility(View.GONE);
           holder.seekBar1.setProgress(0);
           holder.seekBar1.setVisibility(View.GONE);
           holder.per1.setVisibility(View.GONE);

       }
      if(!TextUtils.isEmpty(op2))
      {
          holder.op2.setText(op2);
      }
      else
      {
          holder.op2.setEnabled(false);

          holder.op2.setVisibility(View.GONE);
          holder.seekBar2.setProgress(0);
          holder.seekBar2.setVisibility(View.GONE);
          holder.per2.setVisibility(View.GONE);
      }
       if(!TextUtils.isEmpty(op3))
       {
           holder.op3.setText(op3);
       }
       else
       {
           holder.op3.setEnabled(false);
           holder.op3.setVisibility(View.GONE);
           holder.seekBar3.setProgress(0);
           holder.seekBar3.setVisibility(View.GONE);
           holder.per3.setVisibility(View.GONE);
       }
        if(!TextUtils.isEmpty(op4))
        {
            holder.op4.setText(op4);
        }
        else
        {
            holder.op4.setEnabled(false);
            holder.op4.setVisibility(View.GONE);
            holder.seekBar4.setProgress(0);
            holder.seekBar4.setVisibility(View.GONE);
            holder.per4.setVisibility(View.GONE);
        }

        if(poll.isSeen()==false)
        {

            holder.seekBar1.setProgress(0);
            holder.seekBar2.setProgress(0);
            holder.seekBar3.setProgress(0);
            holder.seekBar4.setProgress(0);
            holder.per1.setText("");
            holder.per2.setText("");
            holder.per3.setText("");
            holder.per4.setText("");
            holder.totalVoters.setText("");
        }


        if(poll.isSeen()==true)
        {
            double per=poll.getOp1v()+poll.getOp2v()+poll.getOp3v()+poll.getOp4v();
            per=100/per;

            holder.seekBar1.setProgress((int) (per*(poll.getOp1v())));
            holder.seekBar2.setProgress((int) (per*(poll.getOp2v())));
            holder.seekBar3.setProgress((int) (per*(poll.getOp3v())));
            holder.seekBar4.setProgress((int) (per*(poll.getOp4v())));
            holder.per1.setText(""+(int) (per*(poll.getOp1v())));
            holder.per2.setText(""+(int) (per*(poll.getOp2v())));
            holder.per3.setText(""+(int) (per*(poll.getOp3v())));
            holder.per4.setText(""+(int) (per*(poll.getOp4v())));
            holder.totalVoters.setText(""+(int) (1/per*100));
           // pollClicked=true;
        }




        String timeago= (String) DateUtils.getRelativeTimeSpanString(poll.getTimestamp().getSeconds()*1000);
        holder.time.setText(timeago);
        holder.liketxt.setText(""+poll.getLikes());
        holder.commenttxt.setText(""+poll.getComments());

        int l=poll.getLikes();
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
        return pollList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SeekBar seekBar1,seekBar2,seekBar3,seekBar4;
        private TextView mainquestion;
        private TextView op1,op2,op3,op4;
        private TextView per1,per2,per3,per4;
        private TextView noofvotes;
        private ImageView likesImg,commentImg;
        private TextView liketxt,commenttxt;
        private TextView time;
        private TextView totalVoters;
        boolean clicked=true;
        // private ProgressBar progressBar;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView, Context ctx, OnItemClickListener listener) {
            super(itemView);
            context=ctx;
            seekBar1=itemView.findViewById(R.id.seek_bar1);
            seekBar2=itemView.findViewById(R.id.seek_bar2);
            seekBar3=itemView.findViewById(R.id.seek_bar3);
            seekBar4=itemView.findViewById(R.id.seek_bar4);
            op1=itemView.findViewById(R.id.tv_option1);
            op2=itemView.findViewById(R.id.tv_option2);
            op3=itemView.findViewById(R.id.tv_option3);
            op4 =itemView.findViewById(R.id.tv_option4);
            per1=itemView.findViewById(R.id.tv_percent1);
            per2=itemView.findViewById(R.id.tv_percent2);
            per3=itemView.findViewById(R.id.tv_percent3);
            per4=itemView.findViewById(R.id.tv_percent4);
            mainquestion=itemView.findViewById(R.id.tv_question);
            totalVoters=itemView.findViewById(R.id.total_voters);


            likesImg=itemView.findViewById(R.id.poll_like_btn);
            commentImg=itemView.findViewById(R.id.poll_cmnt_btn);
            liketxt=itemView.findViewById(R.id.poll_ct_like_txt);
            commenttxt=itemView.findViewById(R.id.poll_ct_cmt_txt);
            time=itemView.findViewById(R.id.poll_time_ago_txt);
            //progressBar=itemView.findViewById(R.id.txt_progressbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemCLick(position);
                        }
                    }

                }
            });

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

            op1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null&&!pollList.get(getAdapterPosition()).isSeen())
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.opClicked(position,"op1v");

                        }
                    }



                }
            });
            op2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null&&!pollList.get(getAdapterPosition()).isSeen())
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION )
                        {
                            listener.opClicked(position,"op2v");




                        }
                    }

                }
            });
            op3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null&&!pollList.get(getAdapterPosition()).isSeen())
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.opClicked(position,"op3v");



                        }
                    }


                }
            });
            op4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null&&!pollList.get(getAdapterPosition()).isSeen())
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.opClicked(position,"op4v");

                        }


                    }
                }
            });
            seekBar1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            seekBar2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            seekBar3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            seekBar4.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

        }
    }
}
