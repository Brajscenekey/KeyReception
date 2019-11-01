package com.key.keyreception.ownerFragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.key.keyreception.R;
import com.key.keyreception.ownerChildFragment.MyjobFragment;
import com.key.keyreception.viewPager.Reserv_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerReservationFragment extends Fragment {


    private ViewPager viewPager;
    private int indicatorWidth = 0;
    private View indicator;
    private TabLayout tabLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        indicator = view.findViewById(R.id.indicator);
        viewPager = view.findViewById(R.id.rev_viewPager);
        tabLayout = view.findViewById(R.id.sliding_tabs);
        Reserv_viewpager reserv_viewpager = new Reserv_viewpager(getChildFragmentManager());
        viewPager.setAdapter(reserv_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLine();



    }

    public void switchPager() {
        viewPager.setCurrentItem(1);
        MyjobFragment fragment = (MyjobFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.rev_viewPager + ":" + viewPager.getCurrentItem());
        fragment.switchApi();
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
