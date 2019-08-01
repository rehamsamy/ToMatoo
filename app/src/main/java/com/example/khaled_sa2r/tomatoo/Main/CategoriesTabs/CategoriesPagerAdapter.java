package com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CategoriesPagerAdapter extends FragmentPagerAdapter{


    public CategoriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllFragmentTab();
            case 1:
                return  new VegetablesFragmentTab();
            case 2:
                return new DatesFragmentTab();
            case 3:
                return new LeafyFragmentTab();

                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "All";
            case 1:
                return "Vegetables";
            case 2:
                return "Dates";
            case  3:
                return "Leafy";
                default:
                    return null;
        }
    }
}
