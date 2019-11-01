package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.model.RatingInfo;

import java.util.List;

public class DetailRatingAdapter extends RecyclerView.Adapter<DetailRatingAdapter.MyViewHolder> {
    private Context mContext;
    private List<RatingInfo> ratingList;


    public DetailRatingAdapter(Context mContext, List<RatingInfo> ratingList) {
        this.mContext = mContext;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.getreviewlayout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        RatingInfo ratingInfo = ratingList.get(i);
        Session session = new Session(mContext);

        holder.count.setText(String.valueOf(ratingInfo.getRating()));
        holder.tv_comment.setText(ratingInfo.getReview());


        if (ratingInfo.getGivenTo().equals(session.getusertype())) {
            holder.tv_sr.setText(R.string.sent);
        } else {
            holder.tv_sr.setText(R.string.receive);

        }

        if (i == 1){
            holder.view1.setVisibility(View.GONE);
        }

        switch (ratingInfo.getRating()) {
            case 1: {
                holder.iv_emoji.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_active_sad_ico));
            }
            break;
            case 2: {
                holder.iv_emoji.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_active_meh_ico));
            }
            break;
            case 3: {
                holder.iv_emoji.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_active_smile3_ico));
            }
            break;
            case 4: {
                holder.iv_emoji.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_active_smile_ico));
            }
            break;
            case 5: {
                holder.iv_emoji.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_active_happy_ico));
            }
            break;
        }

    }


    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView count;
        private TextView tv_comment, tv_sr;
        private ImageView iv_emoji;
        private View view1;

        public MyViewHolder(View view) {
            super(view);
            count = view.findViewById(R.id.count);
            iv_emoji = view.findViewById(R.id.iv_emoji);
            tv_comment = view.findViewById(R.id.tv_comment);
            tv_sr = view.findViewById(R.id.tv_sr);
            view1 = view.findViewById(R.id.view);

        }


    }
}
