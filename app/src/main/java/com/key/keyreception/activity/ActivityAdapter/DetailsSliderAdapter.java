package com.key.keyreception.activity.ActivityAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.key.keyreception.R;
import com.key.keyreception.activity.model.PropertyData;

import java.util.ArrayList;

/**
 * Created by mindiii on 17/7/18.
 */

public class DetailsSliderAdapter extends PagerAdapter {

    private ArrayList<PropertyData.PropertyImgBean> detailsModals;
    private Context context;

    public DetailsSliderAdapter(Context context, ArrayList<PropertyData.PropertyImgBean> detailsModals) {
        this.context = context;
        this.detailsModals = detailsModals;
    }

    @Override
    public int getCount() {
        return detailsModals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.detail_slider, container, false);
        PropertyData.PropertyImgBean propertyImgBean = detailsModals.get(position);
        ImageView imageView = view.findViewById(R.id.imagee);
        if (!propertyImgBean.getPropertyImage().isEmpty()) {
            Glide.with(context).load(propertyImgBean.getPropertyImage()).into(imageView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
