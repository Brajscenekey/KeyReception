package com.key.keyreception.viewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.key.keyreception.ownerChildFragment.MyjobFragment;
import com.key.keyreception.ownerChildFragment.ReservationFragment;

/**
 * Created by Ravi Birla on 04,March,2019
 *
 */
public class Reserv_viewpager extends FragmentPagerAdapter {

    public Reserv_viewpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ReservationFragment();
        } else if (position == 1) {
            fragment = new MyjobFragment();

        }
    /*    else if (position == 2)
        {
            fragment=new CartFragment();
        }*/
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Reservations";

        } else if (position == 1) {
            title = "My Jobs";

        }
       /* else if (position == 2)
        {
            title = "Cart";
        }*/
        return title;
    }
}

