package com.key.keyreception.activity.ActivityAdapter;

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
import com.key.keyreception.activity.model.ServiceCategory;
import com.key.keyreception.R;

import java.util.List;

/**
 * Created by Ravi Birla on 18,December,2018
 */
public class Createjobcat_Adapter extends RecyclerView.Adapter<Createjobcat_Adapter.MyViewHolder> {

    private Context context;
    private List<ServiceCategory> categoryList;

    private checkupdate checkupdate;
    public interface checkupdate {
        void updatecheckcount(List<ServiceCategory> inprodataList);
        }
    public Createjobcat_Adapter(Context context, List<ServiceCategory> categoryList,checkupdate checkupdate)
//            , OnItemListener onItemListener)
    {
        this.context = context;
        this.categoryList = categoryList;
        this.checkupdate =checkupdate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.checkdata, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ServiceCategory s = categoryList.get(position);
        holder.comyorder_tv.setText(s.getTitle());
        Glide.with(context).load(s.getImageurl() + s.getImage()).into(holder.inactivecheck);

        if (s.isselect) {
            holder.rb_selectcategory.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_active_checked));

        } else {
            holder.rb_selectcategory.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_inactive_checked));
        }

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView comyorder_tv;
        ImageView inactivecheck;
        RelativeLayout allrl;
        ImageView rb_selectcategory;
        public MyViewHolder(final View itemView) {
            super(itemView);
            comyorder_tv = itemView.findViewById(R.id.comyorder_tv);
            inactivecheck = itemView.findViewById(R.id.inactivecheck);
            allrl = itemView.findViewById(R.id.allrl);
            rb_selectcategory = itemView.findViewById(R.id.rb_selectcategory);
            allrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1) {
                        ServiceCategory chedata = categoryList.get(getAdapterPosition());
                        if (!chedata.isselect) {
                            chedata.isselect = true;
                            checkupdate.updatecheckcount(categoryList);
                            notifyDataSetChanged();
                        } else {
                            chedata.isselect = false;
                            checkupdate.updatecheckcount(categoryList);
                            notifyDataSetChanged();
                        }
                    }

                }
            });


        }
    }

}
