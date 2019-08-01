package com.example.khaled_sa2r.tomatoo.Main.CategoriesItems;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.khaled_sa2r.tomatoo.R;
import com.example.khaled_sa2r.tomatoo.Main.CategoriesItems.TouchHelper.RecyclerItemTouchHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartActivity extends AppCompatActivity  implements RecyclerItemTouchHelper.ItemTouchHelperInterface {

    @BindView(R.id.back_img) ImageView backImg;
    @BindView(R.id.shopping_cart_recycler) RecyclerView recyclerView;
    @BindView(R.id.checkout_btn) Button checkoutBtn;
    @BindView(R.id.root) ConstraintLayout root;
    ShoppingCartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        adapter=new ShoppingCartAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback helper=new RecyclerItemTouchHelper(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(helper).attachToRecyclerView(recyclerView);
    }



    @OnClick(R.id.back_img)
    void back(View view){
         startActivity(new Intent(getApplicationContext(),CategoryItemDetailsActivity.class));
    }

    @OnClick(R.id.checkout_btn)
    void click(View view){
        startActivity(new Intent(getApplicationContext(),CheckoutActivity.class));
    }



    @Override
    public void swipped(RecyclerView.ViewHolder holder, int direction, int position) {

        Snackbar snackbar = Snackbar
                .make(root,  " removed from cart!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
               // mAdapter.restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }


    }

