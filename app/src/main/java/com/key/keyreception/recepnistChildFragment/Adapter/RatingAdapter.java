package com.key.keyreception.recepnistChildFragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.recepnistChildFragment.model.RatingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private Context context;
    private List<RatingModel> ratingList;
    private String currentTime;

    public RatingAdapter(Context context, List<RatingModel> ratingList,String currentTime) {
        this.context = context;
        this.ratingList = ratingList;
        this.currentTime = currentTime;

    }

    @NonNull
    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rrating_layout, parent, false);
        return new RatingAdapter.MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final RatingAdapter.MyViewHolder holder, final int position) {
        RatingModel ratingModel = ratingList.get(position);
        holder.tv_proname.setText(ratingModel.getFullName());
        holder.tv_rating.setText(String.valueOf(ratingModel.getRating()));
        holder.tv_review.setText(ratingModel.getReview());

        if (ratingModel.getProfileImage().length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.user_img);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context).load(ratingModel.getProfileImage()).apply(options).into(holder.iv_proimg);
        }

        Date cDate = getCurrentDate(currentTime);
        Date eDate = getCurrentDate(ratingModel.getCrd());
        printDifference(eDate, cDate, holder.tv_time);


    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_proname, tv_time, tv_rating, tv_review;
        private ImageView iv_proimg;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tv_proname = itemView.findViewById(R.id.tv_proname);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_review = itemView.findViewById(R.id.tv_review);
            iv_proimg = itemView.findViewById(R.id.iv_proimg);

        }
    }

    private Date getCurrentDate(String cDate) {
        Log.d("DATE", "C> " + cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        try {
            return sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getCrdDate(String cDate) {
        Log.d("DATE", "C> " + cDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            return sdf.parse(cDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void printDifference(Date startDate, Date endDate, TextView textView) {
        long different = endDate.getTime() - startDate.getTime();
        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays != 0) {
            textView.setText(String.valueOf(elapsedDays) + " " + context.getString(R.string.days_ago));
        } else if (elapsedHours != 0) {
            textView.setText(String.valueOf(elapsedHours) + " " + context.getString(R.string.hours_ago));
        } else if (elapsedMinutes != 0) {
            textView.setText(String.valueOf(elapsedMinutes) + " " + context.getString(R.string.minute_ago));
        } else if (elapsedSeconds != 0) {
            textView.setText(String.valueOf(elapsedSeconds) + " " + context.getString(R.string.second_ago));


        }
    }

}
