package com.key.keyreception.recepnistChildFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.ownerChildFragment.adapterchild.EarnAdapter;
import com.key.keyreception.recepnistChildFragment.Adapter.RearnAdapter;
import com.key.keyreception.viewPager.Reserv_earning_viewpager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RearningFragment extends BaseFragment {

    private RearnAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init();
    }

    public void init()
    {
//        RecyclerView recyclerView = getView().findViewById(R.id.appo_recycler_view);
//        adapter = new RearnAdapter(mContext);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
    }
}
