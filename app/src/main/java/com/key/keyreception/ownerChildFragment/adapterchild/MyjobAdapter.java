package com.key.keyreception.ownerChildFragment.adapterchild;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.DetailActivity;
import com.key.keyreception.activity.model.MyJobData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class MyjobAdapter extends RecyclerView.Adapter<MyjobAdapter.MyViewHolder> {

    private Context context;
    private List<MyJobData> jobDataList;

    public MyjobAdapter(Context context, List<MyJobData> jobDataList) {
        this.context = context;
        this.jobDataList = jobDataList;
    }

    @NonNull
    @Override
    public MyjobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myjob_layout, parent, false);
        return new MyjobAdapter.MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final MyjobAdapter.MyViewHolder holder, final int position) {
        MyJobData myJobData = jobDataList.get(position);
        holder.tv_Myjob_proname.setText(myJobData.getPropertyData().get(0).getPropertyName());
        holder.tv_Myjob_address.setText(myJobData.getPropertyData().get(0).getPropertyAddress());
        holder.tv_Myjob_status.setVisibility(View.VISIBLE);
        holder.rprof_img.setVisibility(View.GONE);
        if (myJobData.getPropertyImg().size() > 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_user_ico);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context).load(myJobData.getPropertyImg().get(0).getPropertyImage()).apply(options).into(holder.iv_Myjobhome);
        }
        try {
            if (myJobData.getServiceDate().contains("-")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date date = sdf.parse(myJobData.getServiceDate());
                SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                String fDate = mSDF.format(date);
                holder.tv_Myjob_time.setText(fDate);
            } else {
                holder.tv_Myjob_time.setText(myJobData.getServiceDate());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (myJobData.getStatus() == 1) {
            holder.tv_Myjob_status.setText(context.getResources().getString(R.string.Pending));
            holder.tv_Myjob_status.setTextColor(context.getResources().getColor(R.color.colorPending));
        } else if (myJobData.getStatus() == 2) {
            holder.tv_Myjob_status.setText(context.getResources().getString(R.string.confirm));
            holder.tv_Myjob_status.setTextColor(context.getResources().getColor(R.color.appcolor));

        } else if (myJobData.getStatus() == 3) {
            holder.tv_Myjob_status.setText(context.getResources().getString(R.string.inpro));
            holder.tv_Myjob_status.setTextColor(context.getResources().getColor(R.color.colorInprogress));

        } else {
            if (myJobData.getStatus() == 4) {
                holder.tv_Myjob_status.setText(context.getResources().getString(R.string.complete));
                holder.tv_Myjob_status.setTextColor(context.getResources().getColor(R.color.colorDarkGreen));
            } else {
                holder.tv_Myjob_status.setText(context.getResources().getString(R.string.paymentcomplete));
                holder.tv_Myjob_status.setTextColor(context.getResources().getColor(R.color.colorDarkGreen));
            }
        }

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return jobDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView rprof_img, iv_Myjobhome;
        TextView tv_Myjob_proname, tv_Myjob_address, tv_Myjob_time, tv_Myjob_status;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_Myjob_proname = itemView.findViewById(R.id.tv_Myjob_proname);
            rprof_img = itemView.findViewById(R.id.rprof_img);
            tv_Myjob_address = itemView.findViewById(R.id.tv_Myjob_address);
            tv_Myjob_time = itemView.findViewById(R.id.tv_Myjob_time);
            tv_Myjob_status = itemView.findViewById(R.id.tv_Myjob_status);
            iv_Myjobhome = itemView.findViewById(R.id.iv_Myjobhome);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Session session = new Session(context);
                    MyJobData myJobData = jobDataList.get(getAdapterPosition());
                    session.putjobid(String.valueOf(myJobData.get_id()));
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
