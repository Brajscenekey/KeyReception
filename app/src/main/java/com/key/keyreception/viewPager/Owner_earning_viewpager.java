package com.key.keyreception.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.key.keyreception.ownerChildFragment.EarnFragment;
import com.key.keyreception.ownerChildFragment.MyPropertyFragment;

/**
 * Created by Ravi Birla on 04,March,2019
 *
 */
public class Owner_earning_viewpager extends FragmentPagerAdapter {

    public Owner_earning_viewpager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new EarnFragment();
        } else if (position == 1) {
            fragment = new MyPropertyFragment();
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
            title = "My Property";
        }
       /* else if (position == 2)
        {
            title = "Cart";
        }*/
        return title;
    }
}

