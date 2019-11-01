package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.key.keyreception.R;
import com.key.keyreception.activity.model.HelpModal;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {
    private Context mContext;
    private List<HelpModal.DataBean> faqResponceList;
    private boolean isExpand = false;

    public HelpAdapter(Context mContext, List<HelpModal.DataBean> faqResponceList) {
        this.mContext = mContext;
        this.faqResponceList = faqResponceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_view, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        HelpModal.DataBean helpQuesBean = faqResponceList.get(i);

        holder.tv_no_id.setText(helpQuesBean.getId() + ".");
        holder.tv_question.setText(helpQuesBean.getQuestion());
        holder.tvAns.setText(helpQuesBean.getAnswer());
    }


    @Override
    public int getItemCount() {
        return faqResponceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_no_id;
        private TextView tv_question;
        private TextView tvAns;

        MyViewHolder(View view) {
            super(view);
            tv_no_id = view.findViewById(R.id.tv_no_id);
            tv_question = view.findViewById(R.id.tv_question);
            tvAns = view.findViewById(R.id.tv_ans);
        }
    }
}