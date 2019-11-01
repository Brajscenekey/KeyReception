package com.key.keyreception.ownerChildFragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.key.keyreception.base.BaseFragment;
import com.key.keyreception.R;
import com.key.keyreception.ownerChildFragment.adapterchild.ReservationAdapter;
import com.mikesu.horizontalexpcalendar.HorizontalExpCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationFragment extends BaseFragment {



    private HorizontalExpCalendar horizontalExpCalendar;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    public void init()
    {
        /*recyclerView=getView().findViewById(R.id.reserv_recycler_view);
        adapter = new ReservationAdapter(mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init();
       /* horizontalExpCalendar = (HorizontalExpCalendar) view.findViewById(R.id.calendar);
        horizontalExpCalendar.setHorizontalExpCalListener(new HorizontalExpCalendar.HorizontalExpCalListener() {
            @Override
            public void onCalendarScroll(DateTime dateTime) {
                Log.i(TAG, "onCalendarScroll: " + dateTime.toString());

            }

            @Override
            public void onDateSelected(DateTime dateTime) {
                Log.i(TAG, "onDateSelected: " + dateTime.toString());

            }

            @Override
            public void onChangeViewPager(Config.ViewPagerType viewPagerType) {
                Log.i(TAG, "onChangeViewPager: " + viewPagerType.name());

            }
        });*/
    }
}
