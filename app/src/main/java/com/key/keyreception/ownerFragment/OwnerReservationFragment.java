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
import com.key.keyreception.ownerChildFragment.MyjobFragment;
import com.key.keyreception.viewPager.Reserv_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerReservationFragment extends Fragment {


    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewPager = view.findViewById(R.id.rev_viewPager);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        Reserv_viewpager reserv_viewpager = new Reserv_viewpager(getChildFragmentManager());
        viewPager.setAdapter(reserv_viewpager);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void switchPager() {
        viewPager.setCurrentItem(1);
        MyjobFragment fragment = (MyjobFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.rev_viewPager + ":" + viewPager.getCurrentItem());
        fragment.switchApi();
    }


}
