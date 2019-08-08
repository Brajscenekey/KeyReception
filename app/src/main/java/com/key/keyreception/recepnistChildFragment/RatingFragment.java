package com.key.keyreception.recepnistChildFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.recepnistChildFragment.Adapter.RatingAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends BaseFragment {

    private RatingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    public void init() {
//        RecyclerView recyclerView = getView().findViewById(R.id.appo_recycler_view);
//        adapter = new RatingAdapter(mContext);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
    }
}
