package com.key.keyreception.activity.ActivityAdapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import uk.co.senab.photoview.PhotoView;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Chat> chatList;
    private String myUId;
    private AdapterPositionListener listener;
    private String time = "";

    private Dialog zoomImageDialog;

    public ChattingAdapter(Context mContext, ArrayList<Chat> chatList, String myUId, AdapterPositionListener listener) {
        this.mContext = mContext;
        this.chatList = chatList;
        this.myUId = myUId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_side_view, parent, false);
                holder = new MyChatViewHolder(view);
                return holder;

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left_side_view, parent, false);
                holder = new OtherChatViewHolder(view);
                return holder;

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right_side_view, parent, false);
                holder = new MyChatViewHolder(view);
                return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (myUId.equals(chatList.get(position).uid)) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        //  listener.getInterest(chatList.get(position).banner_date);

        int pos = position - 1;
        int tempPos = (pos == -1) ? pos + 1 : pos;
        Chat chat = chatList.get(position);

        if (myUId.equals(chatList.get(position).uid)) {
            ((MyChatViewHolder) h).myBindData(chat, position, tempPos);
        } else {
            ((OtherChatViewHolder) h).otherBindData(chat, position, tempPos);
        }

        listener.getPosition(position);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    private void openZoomImageDialog(String image) {
        zoomImageDialog = new Dialog(mContext, R.style.MyAppTheme);
        zoomImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        zoomImageDialog.setContentView(R.layout.dialog_zoom_image_view);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.copyFrom(zoomImageDialog.getWindow().getAttributes());
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        zoomImageDialog.getWindow().setAttributes(windowParams);

        PhotoView mphoto_view = zoomImageDialog.findViewById(R.id.img_enlarge);

        ImageView iv_back = zoomImageDialog.findViewById(R.id.iv_back);

        Glide.with(mContext).load(image).into(mphoto_view);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageDialog.dismiss();
            }
        });

        zoomImageDialog.getWindow().setGravity(Gravity.CENTER);
        zoomImageDialog.show();
    }


    public interface AdapterPositionListener {
        void getPosition(int position);
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // My Chat Text Msg
        private RelativeLayout ly_my_image_view;
        private ImageView iv_my_side_img;
        private ProgressBar my_progress;
        private TextView my_message, my_date_time_, tv_days_status;

        MyChatViewHolder(View itemView) {
            super(itemView);
            my_message = itemView.findViewById(R.id.my_message);
            iv_my_side_img = itemView.findViewById(R.id.iv_my_side_img);
            ly_my_image_view = itemView.findViewById(R.id.rl_my_image_view);
            my_progress = itemView.findViewById(R.id.my_progress);
            my_date_time_ = itemView.findViewById(R.id.my_date_time_);
            tv_days_status = itemView.findViewById(R.id.tv_days_status);

            iv_my_side_img.setOnClickListener(this);
        }

        void myBindData(Chat chat, int pos, int tempPos) {

            if (chatList.get(pos).getIsImage() == 1) {
                my_progress.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(chat.imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder_image)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        my_progress.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        my_progress.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_my_side_img);

                SimpleDateFormat sd = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    String time_str = sd.format(new Date((Long) chat.getTimestamp()));
                    time = time_str.replace("am", "AM").replace("pm", "PM");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                my_date_time_.setVisibility(View.VISIBLE);
                my_date_time_.setText(time);
                my_message.setVisibility(View.GONE);
                ly_my_image_view.setVisibility(View.VISIBLE);
            } else if (chat.getIsImage() == 0) {
                my_message.setVisibility(View.VISIBLE);
                my_message.setText(chat.message);
                ly_my_image_view.setVisibility(View.GONE);
                SimpleDateFormat sd = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    String time_str = sd.format(new Date((Long) chat.getTimestamp()));
                    time = time_str.replace("am", "AM").replace("pm", "PM");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                my_date_time_.setText(time);

            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_my_side_img:
                    openZoomImageDialog(chatList.get(getAdapterPosition()).imageUrl);
                    break;


            }
        }

    }

    public class OtherChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout ly_msg_view, ly_other_image_view;
        private ImageView iv_other_side_img;
        private ProgressBar other_progress;
        private TextView other_message, other_date_time_, tv_days_status;

        OtherChatViewHolder(View itemView) {
            super(itemView);
            other_message = itemView.findViewById(R.id.other_message);
            ly_msg_view = itemView.findViewById(R.id.ly_msg_view);
            ly_other_image_view = itemView.findViewById(R.id.ly_other_image_view);
            iv_other_side_img = itemView.findViewById(R.id.iv_other_side_img);
            other_progress = itemView.findViewById(R.id.other_progress);
            other_date_time_ = itemView.findViewById(R.id.other_date_time_);
            tv_days_status = itemView.findViewById(R.id.tv_days_status);
            iv_other_side_img.setOnClickListener(this);

        }

        void otherBindData(Chat chat, int pos, int tempPos) {

            if (chatList.get(pos).getIsImage() == 1) {
                other_progress.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(chat.imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder_image)).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        other_progress.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        other_progress.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_other_side_img);

                SimpleDateFormat sd = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    String time_str = sd.format(new Date((Long) chat.getTimestamp()));
                    time = time_str.replace("am", "AM").replace("pm", "PM");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                other_date_time_.setVisibility(View.VISIBLE);
                other_date_time_.setText(time);
                ly_msg_view.setVisibility(View.GONE);
                ly_other_image_view.setVisibility(View.VISIBLE);
            } else if (chat.getIsImage() == 0) {
                //gone
                other_message.setText(chat.getMessage());
                ly_msg_view.setVisibility(View.VISIBLE);
                ly_other_image_view.setVisibility(View.GONE);
                SimpleDateFormat sd = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    String time_str = sd.format(new Date((Long) chat.getTimestamp()));
                    time = time_str.replace("am", "AM").replace("pm", "PM");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                other_date_time_.setText(time);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_other_side_img:
                    openZoomImageDialog(chatList.get(getAdapterPosition()).imageUrl);
                    break;
            }
        }
    }
}
