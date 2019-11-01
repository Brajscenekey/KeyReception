package com.key.keyreception.viewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.key.keyreception.recepnistChildFragment.RatingFragment;
import com.key.keyreception.recepnistChildFragment.RearningFragment;

/**
 * Created by Ravi Birla on 04,March,2019
 *
 */
public class Reserv_earning_viewpager extends FragmentPagerAdapter {

    public Reserv_earning_viewpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new RearningFragment();
        } else if (position == 1) {
            fragment = new RatingFragment();
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
            title = "Earning";

        } else if (position == 1) {
            title = "Rating";
        }
       /* else if (position == 2)
        {
            title = "Cart";
        }*/
        return title;
    }
}

