package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.activity.model.FaqResponce;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {
    private Context mContext;
    private List<FaqResponce.DataBean> faqResponceList;

    public FaqAdapter(Context mContext, List<FaqResponce.DataBean> faqResponceList) {
        this.mContext = mContext;
        this.faqResponceList = faqResponceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faq_cell, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        FaqResponce.DataBean faqResponce = faqResponceList.get(i);

        holder.tvQuestion.setText(faqResponce.getQuestion());
        holder.tvAns.setText(faqResponce.getAnswer());


        if(faqResponce.getIsOpen().equals("Yes")){
            //faqResponce.setIsOpen("NO");
            holder.iv_collaps.setImageResource(R.drawable.minus_ico);
            holder.tvQuestion.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.rlAns.setVisibility(View.VISIBLE);
        }else{
            //faqResponce.setIsOpen("NO");
            holder.iv_collaps.setImageResource(R.drawable.add_ico);
            holder.tvQuestion.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.rlAns.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return faqResponceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvQuestion;
        private RelativeLayout rlAns;
        private RelativeLayout rltv_question;
        private TextView tvAns;
        private ImageView iv_collaps;

        public MyViewHolder(View view) {
            super(view);
            tvQuestion = view.findViewById(R.id.tv_question);
            rltv_question = view.findViewById(R.id.rltv_question);
            iv_collaps = view.findViewById(R.id.iv_collaps);
            rlAns = view.findViewById(R.id.rl_ans);
            tvAns = view.findViewById(R.id.tv_ans);

            rltv_question.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.rltv_question:
                    FaqResponce.DataBean faqResponce = faqResponceList.get(getAdapterPosition());
                    if(faqResponce.getIsOpen().equals("NO")){
                        faqResponce.setIsOpen("Yes");
                    }else{
                        faqResponce.setIsOpen("NO");
                    }
                    notifyDataSetChanged();
                break;
            }
        }
    }
}
