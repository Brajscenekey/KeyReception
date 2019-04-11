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

import com.key.keyreception.R;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.viewPager.Reserv_earning_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarningFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_earning, container, false);
        return inflater.inflate(R.layout.fragment_owner_message, container, false);

    }



  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ViewPager viewPager = view.findViewById(R.id.rev_earning_viewPager);
        TabLayout tabLayout = view.findViewById(R.id.sliding_rearning_tabs);
        //wrapTabIndicatorToTitle(tabLayout,150,150);
        Reserv_earning_viewpager reserv_earning_viewpager = new Reserv_earning_viewpager(getChildFragmentManager());
        viewPager.setAdapter(reserv_earning_viewpager);
        tabLayout.setupWithViewPager(viewPager);

    }*/

}
