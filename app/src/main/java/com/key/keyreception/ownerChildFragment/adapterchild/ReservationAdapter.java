package com.key.keyreception.ownerChildFragment.adapterchild;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.key.keyreception.Activity.DetailActivity;
import com.key.keyreception.R;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    Context context;



    public ReservationAdapter(Context context)
    {
        this.context = context;

    }

    @NonNull
    @Override
    public ReservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reservationlist_layout, parent, false);
        return new ReservationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationAdapter.MyViewHolder holder, final int position) {


    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(final View itemView) {
            super(itemView);


           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });*/
        }
    }

}
