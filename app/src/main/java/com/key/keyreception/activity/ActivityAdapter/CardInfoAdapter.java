package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.CardData;
import com.key.keyreception.activity.model.StripeSaveCardResponce;

import java.util.List;

/**
 * Created by Ravi Birla MINDIII sys on 24/11/18.
 */

public class CardInfoAdapter extends RecyclerView.Adapter<CardInfoAdapter.ViewHolder> {

    //    private onCardclick onCardclick;
    boolean status = false;
    private List<StripeSaveCardResponce.DataBean> cardList;
    private Context context;
    private CardData cardData;
    private int lastSelectedPosition = -1;
    private CardDetailInterface CardDetailInterface;
    private DeleteSaveCard deleteSaveCard;
    private String checkcode;

    public CardInfoAdapter(Context context, List<StripeSaveCardResponce.DataBean> cardList,String checkcode ,CardDetailInterface cardDetailInterface, DeleteSaveCard deleteSaveCard) {
        this.cardList = cardList;
        this.context = context;
        this.checkcode =checkcode;
//        this.onCardclick=onCardclick;
        this.deleteSaveCard = deleteSaveCard;
        this.CardDetailInterface = cardDetailInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_paydetail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        StripeSaveCardResponce.DataBean dataBean = cardList.get(position);

        holder.swipe.addDrag(SwipeLayout.DragEdge.Right, holder.swipe.findViewById(R.id.Mcdelete_post));

        holder.tv_card_no.setText(dataBean.getLast4());
        holder.tv_cardName.setText(dataBean.getBrand());
        if (!dataBean.isMoreDetail()) {
            holder.iv_check.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_verified));

        } else {
            holder.iv_check.setImageDrawable(context.getResources().getDrawable(R.drawable.default_circle));

        }


    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public interface CardDetailInterface {


        void oncardDetail(int pos, boolean b);
    }

    public interface DeleteSaveCard {

        void ondeleteSaveCard(String id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_card_no,tv_cardName;
        private CardView cardid;
        private ImageView iv_check;
        private SwipeLayout swipe;
        private CardView Mcdelete_post;

        ViewHolder(View itemView) {
            super(itemView);

            tv_card_no = itemView.findViewById(R.id.tv_card_no);
            tv_cardName = itemView.findViewById(R.id.tv_cardName);
            iv_check = itemView.findViewById(R.id.iv_check);
            cardid = itemView.findViewById(R.id.cardid);
            swipe = itemView.findViewById(R.id.swipe);
            Mcdelete_post = itemView.findViewById(R.id.Mcdelete_post);
            cardid.setOnClickListener(this);
            Mcdelete_post.setOnClickListener(this);

            if (checkcode.equals("1")){
                iv_check.setVisibility(View.VISIBLE);
                }
                else {
                iv_check.setVisibility(View.GONE);

            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cardid: {
                    CardDetailInterface.oncardDetail(getAdapterPosition(), false);
                }
                break;
                case R.id.Mcdelete_post: {
                    StripeSaveCardResponce.DataBean dataBean = cardList.get(getAdapterPosition());
                    deleteSaveCard.ondeleteSaveCard(dataBean.getId());
                }
                break;
            }
        }
    }

}




