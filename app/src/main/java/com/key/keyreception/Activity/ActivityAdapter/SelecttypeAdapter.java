package com.key.keyreception.Activity.ActivityAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.key.keyreception.Activity.model.PropertyData;
import com.key.keyreception.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class SelecttypeAdapter extends RecyclerView.Adapter<SelecttypeAdapter.MyViewHolder> {

    private Context context;
    private List<PropertyData> propertyList;
    private PropertyListener propertyListener;
    public interface PropertyListener
    {
        void pos(int i);
    }

    public SelecttypeAdapter(Context context,List<PropertyData> propertyList,PropertyListener propertyListene)
    {
        this.context = context;
        this.propertyList=propertyList;
        this.propertyListener = propertyListene;

    }

    @NonNull
    @Override
    public SelecttypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.selectpropertylayout, parent, false);
        return new SelecttypeAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelecttypeAdapter.MyViewHolder holder, final int position) {
        PropertyData propertyData  = propertyList.get(position);

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            Date date = sdf.parse(propertyData.getPropertyCheckIn());
            Date date1 = sdf.parse(propertyData.getPropertyCheckIn());
            SimpleDateFormat mSDF = new SimpleDateFormat("dd, MMMM hh:mm a", Locale.getDefault());
            String fDate = mSDF.format(date);
            String fDate1 = mSDF.format(date1);
            holder.tv_tin.setText(fDate);
            holder.tv_tout.setText(fDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.tv_name.setText(propertyData.getPropertyName());
        holder.tv_address.setText(propertyData.getPropertyAddress());
        Glide.with(context).load(propertyData.getPropertyImage()).into(holder.imageView);

        if (propertyData.isclick)
        {
            holder.radioButton.setChecked(true);

        }
        else {
            holder.radioButton.setChecked(false);

        }



    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return propertyList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView tv_tin,tv_tout,tv_name,tv_address;
        ImageView imageView;
        RelativeLayout rl_selectproperty;
        MyViewHolder(final View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_selecttype_name);
            tv_address = itemView.findViewById(R.id.tv_selecttype_address);
            tv_tin = itemView.findViewById(R.id.tv_selecttype_intime);
            tv_tout = itemView.findViewById(R.id.tv_selecttype_outtime);
            radioButton = itemView.findViewById(R.id.rb_selecttype);
            imageView = itemView.findViewById(R.id.iv_selecttype_image);
            rl_selectproperty = itemView.findViewById(R.id.rl_selectproperty);


            rl_selectproperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition() != -1)
                    {

                        PropertyData propertyData = propertyList.get(getAdapterPosition());

                        for (int i = 0; i < propertyList.size(); i++) {
                             propertyList.get(i).isclick = false;
                             }
                             propertyData.isclick = true;
                             propertyListener.pos(getAdapterPosition());
                             notifyDataSetChanged();


                    }
                }
            });
        }
    }

}
