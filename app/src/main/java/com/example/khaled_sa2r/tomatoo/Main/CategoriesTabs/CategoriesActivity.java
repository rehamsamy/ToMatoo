package com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.khaled_sa2r.tomatoo.Main.MainPageActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.AboutUsActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.ContactUsActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Communications.HowToUseActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.CompareList.MyCompareListActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Profile.ProfileActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Settings.SettingActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.MyOrderActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.Notifications.NotificationsActivity;
import com.example.khaled_sa2r.tomatoo.MenuActivities.WishList.MyWishListActivity;
import com.example.khaled_sa2r.tomatoo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.nav_filter_img)ImageView navFilterImg;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nv) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    Dialog mDialog;
    CategoriesPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        adapter =new CategoriesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.isDrawerOpen(R.drawable.nav_menu1);

        navigationView.setNavigationItemSelectedListener(this);




    }

    @OnClick(R.id.nav_filter_img)
    void click(View view){
        mDialog=new Dialog(this);
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.sort_filter_popup_layout);
        mDialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent intent = new Intent(CategoriesActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_wishlist) {
            startActivity(new Intent(CategoriesActivity.this,MyWishListActivity.class));

        } else if (id == R.id.nav_comparelist) {
            startActivity(new Intent(CategoriesActivity.this,MyCompareListActivity.class));

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(CategoriesActivity.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(CategoriesActivity.this,NotificationsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(CategoriesActivity.this,SettingActivity.class));

        }else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(CategoriesActivity.this, AboutUsActivity.class));

        }else if (id == R.id.nav_how_to_use) {
            startActivity(new Intent(CategoriesActivity.this, HowToUseActivity.class));


        }else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(CategoriesActivity.this, ContactUsActivity.class));

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
