package com.key.keyreception.ownerChildFragment.adapterchild;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.PropertyData;
import com.key.keyreception.activity.owner.EditPropertyActivity;
import com.key.keyreception.activity.owner.PropertyDetailActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder> {

    private Context context;
    private List<PropertyData> propertylist;
    private Deletelist deletelist;

    public MyPropertyAdapter(Context context, List<PropertyData> propertylist, Deletelist deletelist) {
        this.context = context;
        this.propertylist = propertylist;
        this.deletelist = deletelist;
    }

    @NonNull
    @Override
    public MyPropertyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myproperty_layout, parent, false);
        return new MyPropertyAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPropertyAdapter.MyViewHolder holder, final int position) {
        PropertyData propertyData = propertylist.get(position);
        holder.tv_myprop_proname.setText(propertyData.getPropertyName());
        holder.tv_myprop_address.setText(propertyData.getPropertyAddress());
        if (propertyData.getPropertyImg().size() != 0) {
            Glide.with(context).load(propertyData.getPropertyImg().get(0).getPropertyImage()).into(holder.iv_myproperty_image);
        } else {
            Glide.with(context).load(R.drawable.ic_new_big_placeholder_img).into(holder.iv_myproperty_image);
        }

    }

    @Override
    public int getItemCount() {
        return propertylist.size();
    }

    public interface Deletelist {
        void deletpos(int i);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_myproperty_image, iv_myproperty_editimage, iv_myproperty_deleteimage;
        TextView tv_myprop_proname, tv_myprop_address;

        public MyViewHolder(final View itemView) {
            super(itemView);
            iv_myproperty_image = itemView.findViewById(R.id.iv_myproperty_image);
            tv_myprop_proname = itemView.findViewById(R.id.tv_myprop_proname);
            tv_myprop_address = itemView.findViewById(R.id.tv_myprop_address);
            iv_myproperty_editimage = itemView.findViewById(R.id.iv_myproperty_editimage);
            iv_myproperty_deleteimage = itemView.findViewById(R.id.iv_myproperty_deleteimage);
            iv_myproperty_deleteimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletelist.deletpos(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            iv_myproperty_editimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PropertyData propertyData = propertylist.get(getAdapterPosition());
                    Intent intent = new Intent(context, EditPropertyActivity.class);
                    intent.putExtra("propertyName", propertyData.getPropertyName());
                    intent.putExtra("bedroom", propertyData.getBedroom());
                    intent.putExtra("intent", "1");
                    intent.putExtra("bathroom", propertyData.getBathroom());
                    intent.putExtra("propertySize", propertyData.getPropertySize());
                    intent.putExtra("propertyAddress", propertyData.getPropertyAddress());
                    intent.putExtra("propertyLat", propertyData.getPropertyLat());
                    intent.putExtra("propertyLong", propertyData.getPropertyLong());
                    intent.putExtra("propertyid", String.valueOf(propertyData.get_id()));
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST", (Serializable) propertyData.getPropertyImg());
                    intent.putExtra("BUNDLE", args);
                    ((Activity) context).startActivityForResult(intent, 10006);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PropertyData propertyData = propertylist.get(getAdapterPosition());
                    Intent intent = new Intent(context, PropertyDetailActivity.class);
                    intent.putExtra("propertyid", String.valueOf(propertyData.get_id()));
                    ((Activity) context).startActivityForResult(intent, 10006);
                }
            });
        }
    }

}
