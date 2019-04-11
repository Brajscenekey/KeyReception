package com.key.keyreception.ownerChildFragment.adapterchild;

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
public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder> {

    Context context;



    public MyPropertyAdapter(Context context)
    {
        this.context = context;

    }

    @NonNull
    @Override
    public MyPropertyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.myproperty_layout, parent, false);
        return new MyPropertyAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPropertyAdapter.MyViewHolder holder, final int position) {


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
