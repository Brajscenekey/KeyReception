package com.key.keyreception.activity.ActivityAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.ServiceCategory;

import java.util.List;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class ServicetypeAdapter extends RecyclerView.Adapter<ServicetypeAdapter.MyViewHolder> {

    private Context context;
    private List<ServiceCategory> categoryList;
    private CatgoryListener catgoryListener;

    public ServicetypeAdapter(Context context, List<ServiceCategory> categoryList, CatgoryListener catgoryListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.catgoryListener = catgoryListener;
    }

    @NonNull
    @Override
    public ServicetypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.servicelayout, parent, false);
        return new ServicetypeAdapter.MyViewHolder(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ServicetypeAdapter.MyViewHolder holder, final int position) {
        ServiceCategory category = categoryList.get(position);

        holder.tv_cattitle.setText(category.getTitle());
        Glide.with(context).load(category.getImageurl() + category.getImage()).into(holder.iv_catimg);

        if (category.isselect) {
            holder.tv_cattitle.setTextColor(context.getResources().getColor(R.color.colorwhite));
            holder.iv_catimg.setColorFilter(context.getResources().getColor(R.color.colorwhite));
            holder.rl_post_service.setBackground(context.getDrawable(R.drawable.cornergrayback));
        } else {
            holder.tv_cattitle.setTextColor(context.getResources().getColor(R.color.colorDarkgray));
            holder.iv_catimg.setColorFilter(context.getResources().getColor(R.color.colorDarkgray));
            holder.rl_post_service.setBackground(context.getDrawable(R.drawable.cornerwhiteback));

        }

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface CatgoryListener {
        void categoryid(String id, String s,View view);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_catimg;
        TextView tv_cattitle;
        RelativeLayout rl_post_service;

        MyViewHolder(final View itemView) {
            super(itemView);

            iv_catimg = itemView.findViewById(R.id.iv_categoryimg);
            tv_cattitle = itemView.findViewById(R.id.tv_categorytitle);


            rl_post_service = itemView.findViewById(R.id.rl_post_service);


            rl_post_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != -1) {

                        ServiceCategory serviceCategory = categoryList.get(getAdapterPosition());

                        for (int i = 0; i < categoryList.size(); i++) {
                            categoryList.get(i).isselect = false;
                        }
                        serviceCategory.isselect = true;
                        catgoryListener.categoryid(String.valueOf(serviceCategory.get_id()), "check",view);
                        notifyDataSetChanged();


                    }
                }
            });

        }
    }

}
