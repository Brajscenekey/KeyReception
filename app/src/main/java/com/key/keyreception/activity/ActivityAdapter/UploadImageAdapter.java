package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.UploadImageModal;

import java.util.ArrayList;


/**
 * Created by Ravi Birla on 07,March,2019
 */
public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<UploadImageModal> uploadImageModalArrayList;
    private Deletelist deletelist;
    private DeleteProperty deleteProperty;

    public UploadImageAdapter(Context context, ArrayList<UploadImageModal> uploadImageModalArrayList, Deletelist deletelist, DeleteProperty deleteProperty) {
        this.uploadImageModalArrayList = uploadImageModalArrayList;
        this.context = context;
        this.deletelist = deletelist;
        this.deleteProperty = deleteProperty;
    }

    @NonNull
    @Override
    public UploadImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.uploadimagelay, parent, false);
        return new UploadImageAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UploadImageAdapter.MyViewHolder holder, final int position) {

        UploadImageModal uploadImageModal = uploadImageModalArrayList.get(position);

        Glide.with(context).load(uploadImageModal.propertyImage).into(holder.iv_upload_photo);

    }

    //TOTAL SPACECRAFTS
    @Override
    public int getItemCount() {
        return uploadImageModalArrayList.size();
    }

    public interface Deletelist {
        void deletpos(int i);
    }

    public interface DeleteProperty {
        void deletproperty(int i);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_upload_photo;
        private ImageView iv_cancle;


        MyViewHolder(final View itemView) {
            super(itemView);

            iv_upload_photo = itemView.findViewById(R.id.iv_upload_photo);
            iv_cancle = itemView.findViewById(R.id.iv_cancle);
            iv_cancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_cancle:
                    if (getAdapterPosition() == 0) {
                        deleteProperty.deletproperty(0);

                    } else {
                        deletelist.deletpos(getAdapterPosition());
                    }

                    break;
            }
        }
    }

}
