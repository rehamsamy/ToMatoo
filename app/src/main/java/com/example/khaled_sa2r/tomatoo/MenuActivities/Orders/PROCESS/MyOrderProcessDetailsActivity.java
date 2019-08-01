package com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.PROCESS;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.COMPELETED.MyOrderOrderAdapter;
import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderProcessDetailsActivity extends AppCompatActivity {

    @BindView(R.id.recycler_order) RecyclerView recyclerView;
    MyOrderOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_process_details);
        ButterKnife.bind(this);
        adapter=new MyOrderOrderAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
