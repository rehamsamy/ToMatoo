package com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.ONWAY;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.ONWAY.MyOrderOnWayAdapter;
import com.example.khaled_sa2r.tomatoo.R;

public class MyOrderOnWayFragment extends Fragment {
    MyOrderOnWayAdapter adapter;
   RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.myorder_onway_fragment,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_onway);
        adapter=new MyOrderOnWayAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
}
