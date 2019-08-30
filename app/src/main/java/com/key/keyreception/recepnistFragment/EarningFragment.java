package com.key.keyreception.recepnistFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.viewPager.Reserv_earning_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningFragment extends BaseFragment {


    private ViewPager viewPager;
    private int indicatorWidth = 0;
    private View indicator;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_earning, container, false);
        return inflater.inflate(R.layout.fragment_earning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        indicator = view.findViewById(R.id.indicator);
        viewPager = view.findViewById(R.id.rev_earning_viewPager);
        tabLayout = view.findViewById(R.id.sliding_tabs);
        Reserv_earning_viewpager reserv_earning_viewpager = new Reserv_earning_viewpager(getChildFragmentManager());
        viewPager.setAdapter(reserv_earning_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLine();


    }
    private void tabLine()
    {

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                indicator.setLayoutParams(indicatorParams);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)indicator.getLayoutParams();
                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+i) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                params.rightMargin = (int) translationOffset;
                indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
