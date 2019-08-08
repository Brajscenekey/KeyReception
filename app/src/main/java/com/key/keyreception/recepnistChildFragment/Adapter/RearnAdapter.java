package com.key.keyreception.recepnistChildFragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.key.keyreception.recepnistChildFragment.model.EarningData;

import java.util.ArrayList;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class RearnAdapter extends RecyclerView.Adapter<RearnAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<EarningData> earnlist;

    public RearnAdapter(Context context, ArrayList<EarningData> earnlist) {
        this.context = context;
        this.earnlist = earnlist;
    }

    @NonNull
    @Override
    public RearnAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rearning_layout, parent, false);
        return new RearnAdapter.MyViewHolder(v);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final RearnAdapter.MyViewHolder holder, final int position) {
        EarningData earningData = earnlist.get(position);
        holder.tv_earn_proname.setText(earningData.getSenderDetail().get(position).getFullName());
        holder.tv_earn_address.setText(earningData.getCategory().get(position).getTitle());
        if (String.valueOf(earningData.getAmount()).contains("$")) {
            holder.tv_earn_money.setText(String.valueOf(earningData.getAmount()));
        } else {
            holder.tv_earn_money.setText("$" + String.valueOf(earningData.getAmount()));

        }

        holder.tv_earn_des.setText(earningData.getJobDetail().getDescription());

        if (earningData.getSenderDetail().get(position).getProfileImage().length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_user_ico);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context).load(earningData.getSenderDetail().get(position).getProfileImage()).apply(options).into(holder.iv_earnimage);
        }
    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return earnlist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_earnimage;
        TextView tv_earn_proname, tv_earn_money, tv_earn_address, tv_earn_des;

        public MyViewHolder(final View itemView) {
            super(itemView);
            iv_earnimage = itemView.findViewById(R.id.iv_earnimage);
            tv_earn_proname = itemView.findViewById(R.id.tv_earn_proname);
            tv_earn_money = itemView.findViewById(R.id.tv_earn_money);
            tv_earn_address = itemView.findViewById(R.id.tv_earn_address);
            tv_earn_des = itemView.findViewById(R.id.tv_earn_des);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EarningData earningData = earnlist.get(getAdapterPosition());
                    Intent intent = new Intent(context, NotiDetailActivity.class);
                    intent.putExtra("notificationDetail_Jobid", String.valueOf(earningData.getJobId()));
                    context.startActivity(intent);
                }
            });*/
        }
    }

}
