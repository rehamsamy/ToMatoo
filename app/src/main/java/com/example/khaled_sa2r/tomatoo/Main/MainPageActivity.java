package com.example.khaled_sa2r.tomatoo.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.AboutUsActivity;
import com.example.khaled_sa2r.tomatoo.Main.Adapters.ViewPagerAdapter;
import com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs.CategoriesActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.ContactUsActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.HowToUseActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.CompareList.MyCompareListActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Profile.ProfileActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Settings.SettingActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.WishList.MyWishListActivity;
import com.example.khaled_sa2r.tomatoo.MyBase.MyBaseActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.MyOrderActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Notifications.NotificationsActivity;
import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPageActivity extends MyBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout indicator;
    @BindView(R.id.see_all) TextView sellAll;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private String [] imageUrls = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/track-439f2.appspot.com/o/5octl8MqYUhc0CUstFDJ3rPTzPA3.jpg?alt=media&token=9b2d7c93-577c-4955-b5c2-1371addc2f62",
            "https://firebasestorage.googleapis.com/v0/b/track-439f2.appspot.com/o/LShaLtNkrWXjqRsk6FXLEmSlA4k2.jpg?alt=media&token=8ae65a09-3794-484e-88bc-0c917927392f",
            "file:///E:/khaled/Courses/Android/Tomatoo/Tomato/drawable-ldpi/home_ads.png"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this,imageUrls);
        viewPager.setAdapter(adapter);

        indicator=(TabLayout)findViewById(R.id.indicator);   //indicator && Selector
        indicator.setupWithViewPager(viewPager, true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(MainPageActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_wishlist) {
            startActivity(new Intent(MainPageActivity.this,MyWishListActivity.class));
        } else if (id == R.id.nav_comparelist) {
            startActivity(new Intent(MainPageActivity.this, com.example.khaled_sa2r.tomatoo.MyCompareListActivity.class));

        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(MainPageActivity.this,MyOrderActivity.class));

        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(MainPageActivity.this,NotificationsActivity.class));


        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainPageActivity.this,SettingActivity.class));



        }else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(MainPageActivity.this, AboutUsActivity.class));



        }else if (id == R.id.nav_how_to_use) {
            startActivity(new Intent(MainPageActivity.this, HowToUseActivity.class));


        }else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(MainPageActivity.this, ContactUsActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick(R.id.see_all)
    void click(){
        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
    }
}
