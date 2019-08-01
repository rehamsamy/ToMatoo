package com.example.khaled_sa2r.tomatoo.MenuActivities.Orders;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.MyOrderPagerAdapter;
import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderActivity extends MyBaseActivity {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    MyOrderPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        adapter=new MyOrderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    }
}
