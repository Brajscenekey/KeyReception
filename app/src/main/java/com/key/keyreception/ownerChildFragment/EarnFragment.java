package com.key.keyreception.ownerChildFragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.R;
import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.ownerChildFragment.adapterchild.EarnAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarnFragment extends BaseFragment {

    private EarnAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    public void init() {
//        RecyclerView recyclerView = getView().findViewById(R.id.appo_recycler_view);
//        adapter = new EarnAdapter(mContext);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
    }
}
