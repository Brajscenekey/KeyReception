package com.key.keyreception.activity.ActivityAdapter;

import android.annotation.SuppressLint;
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
public class GetAddServiceAdapter extends RecyclerView.Adapter<GetAddServiceAdapter.MyViewHolder> {

    private Context context;

    public GetAddServiceAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GetAddServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.getaddservicelayout, parent, false);
        return new GetAddServiceAdapter.MyViewHolder(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final GetAddServiceAdapter.MyViewHolder holder, final int position) {

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return 2;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(final View itemView) {
            super(itemView);


        }
    }

}
