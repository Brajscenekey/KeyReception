package com.key.keyreception.activity.ActivityAdapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.activity.model.GetAdditionalModel;

import java.util.List;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class GetAddServiceAdapter extends RecyclerView.Adapter<GetAddServiceAdapter.MyViewHolder> {

    private Context context;
    private List<GetAdditionalModel> subCategoryList;

    public GetAddServiceAdapter(Context context,List<GetAdditionalModel> subCategoryList) {
        this.context = context;
        this.subCategoryList = subCategoryList;
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

        GetAdditionalModel model =subCategoryList.get(position);
        holder.tv_addcategory.setText(model.getTitle());
        if (Integer.valueOf(model.getQuantity()) < 10) {
            holder.tv_categoryprice.setText(String.format("$%s x 0%s", model.getPrice(), model.getQuantity()));
        } else {
            holder.tv_categoryprice.setText(String.format("$%s x %s", model.getQuantity(), model.getPrice()));

        }

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView tv_addcategory,tv_categoryprice;

        MyViewHolder(final View itemView) {
            super(itemView);
            tv_categoryprice = itemView.findViewById(R.id.tv_categoryprice);
            tv_addcategory = itemView.findViewById(R.id.tv_addcategory);

        }
    }

}
