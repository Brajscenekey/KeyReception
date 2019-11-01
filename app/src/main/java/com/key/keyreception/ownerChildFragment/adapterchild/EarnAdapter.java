package com.key.keyreception.ownerChildFragment.adapterchild;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.R;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class EarnAdapter extends RecyclerView.Adapter<EarnAdapter.MyViewHolder> {

    private Context context;
    public EarnAdapter(Context context)
    {
        this.context = context;

    }
    @NonNull
    @Override
    public EarnAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.ownearning_layout, parent, false);
        return new EarnAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EarnAdapter.MyViewHolder holder, final int position) {
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
