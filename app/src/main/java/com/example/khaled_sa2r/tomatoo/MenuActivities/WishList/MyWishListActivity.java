package com.example.khaled_sa2r.tomatoo.MenuActivities.WishList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.khaled_sa2r.tomatoo.ItemModel;
import com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs.AllFragmentAdapter;
import com.example.khaled_sa2r.tomatoo.R;

import java.util.ArrayList;

public class MyWishListActivity extends AppCompatActivity implements AllFragmentAdapter.OnItemInterface, WishlistAdapter.OnItemInterface {

    ArrayList<ItemModel> models;
    ItemModel itemModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);


        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_all);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        itemModel=new ItemModel("Apple","Fruit","20$","hellofd dujdsgfdfhdzs xz ghdcvzcc");
//        models.add(itemModel);
//        models.add(itemModel);
//        models.add(itemModel);
//        models.add(itemModel);

        WishlistAdapter adapter=new WishlistAdapter(this,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
