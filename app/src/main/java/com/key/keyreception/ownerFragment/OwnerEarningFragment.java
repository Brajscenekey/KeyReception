package com.key.keyreception.ownerFragment;


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
import com.key.keyreception.viewPager.Owner_earning_viewpager;
import com.key.keyreception.viewPager.Reserv_earning_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerEarningFragment extends BaseFragment {


    public OwnerEarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_owner_earning, container, false);
        return inflater.inflate(R.layout.fragment_owner_message, container, false);

    }

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ViewPager viewPager = view.findViewById(R.id.own_earning_viewPager);
        TabLayout tabLayout = view.findViewById(R.id.sliding_oearning_tabs);
        //wrapTabIndicatorToTitle(tabLayout,150,150);
        Owner_earning_viewpager owner_earning_viewpager = new Owner_earning_viewpager(getChildFragmentManager());
        viewPager.setAdapter(owner_earning_viewpager);
        tabLayout.setupWithViewPager(viewPager);

    }*/


}
