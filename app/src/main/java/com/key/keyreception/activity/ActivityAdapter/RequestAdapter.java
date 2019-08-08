package com.key.keyreception.activity.ActivityAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.RequestData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private List<RequestData> requestDataList;
    private ListenerActiveData listenerActiveData;

    public RequestAdapter(Context context, List<RequestData> requestDataList, ListenerActiveData listenerActiveData) {
        this.context = context;
        this.requestDataList = requestDataList;
        this.listenerActiveData = listenerActiveData;
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myjob_layout, parent, false);
        return new RequestAdapter.MyViewHolder(v);
    }

    @SuppressLint({"NewApi", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull final RequestAdapter.MyViewHolder holder, final int position) {
        RequestData requestData = requestDataList.get(position);
        holder.requestarrow.setVisibility(View.VISIBLE);

        RequestOptions option = new RequestOptions();
        option.placeholder(R.drawable.ic_user_ico);
        option.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (requestData.getPropertyImg().size() > 0) {
            Glide.with(context).load(requestData.getPropertyImg().get(0).getPropertyImage()).apply(option).into(holder.iv_Myjobhome);
        }

        if (!requestData.getJobDetail().isEmpty()) {
            holder.tv_Myjob_address.setText(requestData.getJobDetail().get(0).getAddress());
            holder.tv_reserv_proname.setText(requestData.getJobDetail().get(0).getPropertyName());
        }
        try {
            if (!requestData.getJobDetail().isEmpty()) {
                if (requestData.getJobDetail().get(0).getServiceDate().contains("-")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    Date date = sdf.parse(requestData.getJobDetail().get(position).getServiceDate());
                    SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
                    String fDate = mSDF.format(date);
                    holder.tv_Myjob_time.setText(fDate);
                } else {
                    holder.tv_Myjob_time.setText(requestData.getJobDetail().get(0).getServiceDate());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (requestData.getSenderDetail().get(position).getProfileImage().length() != 0) {
            RequestOptions options = new RequestOptions();
            options.placeholder(R.drawable.ic_user_ico);
            options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(context).load(requestData.getSenderDetail().get(position).getProfileImage()).apply(options).into(holder.iv_rprof_img);
        }
    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return requestDataList.size();
    }

    public interface ListenerActiveData {
        void pos(int id, View view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_rprof_img, requestarrow, iv_Myjobhome;
        TextView tv_reserv_proname, tv_Myjob_address, tv_Myjob_time;
        RelativeLayout rl_request;

        MyViewHolder(final View itemView) {
            super(itemView);

            tv_reserv_proname = itemView.findViewById(R.id.tv_Myjob_proname);
            tv_Myjob_address = itemView.findViewById(R.id.tv_Myjob_address);
            tv_Myjob_time = itemView.findViewById(R.id.tv_Myjob_time);
            iv_rprof_img = itemView.findViewById(R.id.rprof_img);
            iv_Myjobhome = itemView.findViewById(R.id.iv_Myjobhome);
            rl_request = itemView.findViewById(R.id.rl_request);
            requestarrow = itemView.findViewById(R.id.requestarrow);
            rl_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerActiveData.pos(getAdapterPosition(), view);
                }
            });


        }
    }

}
