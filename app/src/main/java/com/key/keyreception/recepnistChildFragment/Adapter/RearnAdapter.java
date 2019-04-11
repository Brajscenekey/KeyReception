package com.key.keyreception.recepnistChildFragment.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.R;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class RearnAdapter extends RecyclerView.Adapter<RearnAdapter.MyViewHolder> {

    Context context;



    public RearnAdapter(Context context)
    {
        this.context = context;

    }

    @NonNull
    @Override
    public RearnAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rearning_layout, parent, false);
        return new RearnAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RearnAdapter.MyViewHolder holder, final int position) {


    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(final View itemView) {
            super(itemView);


        }
    }

}
