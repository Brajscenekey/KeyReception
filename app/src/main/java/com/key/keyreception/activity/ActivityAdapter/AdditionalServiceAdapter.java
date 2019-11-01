package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.activity.model.SubSevices;

import java.util.List;

/**
 * Created by Ravi Birla on 07,March,2019
 */
public class AdditionalServiceAdapter extends RecyclerView.Adapter<AdditionalServiceAdapter.MyViewHolder> {

    private Context context;
    private List<SubSevices> list;
    private ListenerAdditionaldata listenerAdditionaldata;

    public interface ListenerAdditionaldata{
         void getpos(int i,View view);
    }

    public AdditionalServiceAdapter(Context context, List<SubSevices> list,ListenerAdditionaldata listenerAdditionaldata) {
        this.context = context;
        this.list = list;
        this.listenerAdditionaldata =listenerAdditionaldata;
    }

    @NonNull
    @Override
    public AdditionalServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.additionalservice, parent, false);
        return new AdditionalServiceAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdditionalServiceAdapter.MyViewHolder holder, final int position) {


        SubSevices sevices = list.get(position);

        if (sevices.count < 10) {
            holder.tv_quantity.setText(String.valueOf("0" + sevices.getCount()));
        } else {
            holder.tv_quantity.setText(String.valueOf(sevices.getCount()));

        }

        holder.tv_addcategory.setText(sevices.getTitle());
        holder.tv_categoryprice.setText(String.format("$%s", sevices.getPrice()));
        if (sevices.isselect) {
            holder.rl_service.setBackground(context.getResources().getDrawable(R.drawable.activeservice));
            holder.iv_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.active_check_ico));
            holder.tv_addcategory.setTextColor(context.getResources().getColor(R.color.colorwhite));
            holder.tv_categoryprice.setTextColor(context.getResources().getColor(R.color.colorwhite));
            holder.rl_quantity.setVisibility(View.VISIBLE);

        } else {
            holder.rl_service.setBackground(context.getResources().getDrawable(R.drawable.cornerwhiteback));
            holder.tv_addcategory.setTextColor(context.getResources().getColor(R.color.colorblack));
            holder.iv_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.inactive_ceck_ico));
            holder.tv_categoryprice.setTextColor(context.getResources().getColor(R.color.colorgray));
            holder.rl_quantity.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RelativeLayout rl_service, rl_quantity;
        private LinearLayout ll_addservice;
        private TextView tv_addcategory, tv_categoryprice, tv_quantity;
        private ImageView iv_circle, iv_add, iv_minus;

        MyViewHolder(final View itemView) {
            super(itemView);

            rl_service = itemView.findViewById(R.id.rl_service);
            ll_addservice = itemView.findViewById(R.id.ll_addservice);
            rl_quantity = itemView.findViewById(R.id.rl_quantity);
            tv_addcategory = itemView.findViewById(R.id.tv_addcategory);
            tv_categoryprice = itemView.findViewById(R.id.tv_categoryprice);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            iv_add = itemView.findViewById(R.id.iv_add);
            iv_minus = itemView.findViewById(R.id.iv_minu);
            iv_circle = itemView.findViewById(R.id.iv_circle);
            rl_service.setOnClickListener(this);
            iv_add.setOnClickListener(this);
            iv_minus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.rl_service: {
                    if (getAdapterPosition() != -1) {
                        SubSevices sevices = list.get(getAdapterPosition());
                        if (list.get(getAdapterPosition()).isIsselect()) {
                            sevices.isselect = false;
                            listenerAdditionaldata.getpos(getAdapterPosition(),view);
                            notifyItemChanged(getAdapterPosition());
                            notifyDataSetChanged();
                        } else {
                            sevices.isselect = true;
                            listenerAdditionaldata.getpos(getAdapterPosition(),view);
                            notifyItemChanged(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }

                }
                break;

                case R.id.iv_add: {
                    SubSevices sevices = list.get(getAdapterPosition());
                    int test_count = sevices.getCount();
                    test_count = test_count + 1;
                    if (test_count != 0) {
                        sevices.setCount(test_count);
                        listenerAdditionaldata.getpos(getAdapterPosition(),view);
                        notifyItemChanged(getAdapterPosition());
                    }
                }
                break;
                case R.id.iv_minu: {
                    SubSevices sevices = list.get(getAdapterPosition());
                    int test_count = sevices.getCount();
                    test_count = test_count - 1;
                    if (test_count != 0) {
                        sevices.setCount(test_count);
                        listenerAdditionaldata.getpos(getAdapterPosition(),view);
                        notifyItemChanged(getAdapterPosition());
                    }

                }
                break;

            }
        }
    }

}
