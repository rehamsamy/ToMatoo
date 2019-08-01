package com.example.khaled_sa2r.tomatoo.MenuActivities.Orders;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.COMPELETED.MyOrderCompletedFragment;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.ONWAY.MyOrderOnWayFragment;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.PROCESS.MyOrderProcessFragment;

public class MyOrderPagerAdapter  extends FragmentPagerAdapter{


    public MyOrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos){
            case 0:
                return new MyOrderProcessFragment();
            case 1:
                return  new MyOrderOnWayFragment();
                case 2:
            return new MyOrderCompletedFragment();
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "  Process   ";
            case 1:
                return "On Way   ";
            case 2:
                return "   Completed";
                default:
                    return null;

        }

    }
}
