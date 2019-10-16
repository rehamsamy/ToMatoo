package com.tomatoo.Main.CategoriesItems;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.AddToCartResponseModel;
import com.tomatoo.Models.CartProductsModel;
import com.tomatoo.Models.CheckOutListModel;
import com.tomatoo.R;
import com.tomatoo.Main.CategoriesItems.TouchHelper.RecyclerItemTouchHelper;
import com.tomatoo.interfaces.RecyclerItemClickListner;
import com.tomatoo.interfaces.RecyclerTouchHelper;
import com.tomatoo.interfaces.RecyclerTouchHelperListner;
import com.tomatoo.utils.DealingWithCartItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends AppCompatActivity implements RecyclerTouchHelperListner {

    @BindView(R.id.cart_recycler_id)
    RecyclerView cart_recycler;
    @BindView(R.id.cart_back_txtV_id)
    ImageView back;
    //    @BindView(R.id.coordinator_layout)
//    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.checkOut_btn_id)
    Button checkout_btn;
    @BindView(R.id.delivery_fee_val_txtV)
    TextView delivery_fee_txtV;
    @BindView(R.id.cartNo_data_txtV_id)
    TextView cartNo_data_txtV;
    @BindView(R.id.cart_progress_id)
    ProgressBar cart_progress;

    public static TextView items_price;
    public static TextView total_cart_price_txtV;

    private int cart_id;
    public static double total = 0.0;
    double delivery_fee_val = 0.0;
    public static double total_cart_val = 0.0;
    List<CartProductsModel.Product> list;
    List<CheckOutListModel> check_list;
    DealingWithCartItem dealingWithCartItem;

    CartAdapter adapter;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        items_price = findViewById(R.id.cart_items_price_val_txtV);
        total_cart_price_txtV = findViewById(R.id.cart_total_price_val_txtV);
        dealingWithCartItem = new DealingWithCartItem(this);

        getCartData();
    }

    private void buildRecyclerView(List<CartProductsModel.Product> list) {
        cart_recycler.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));
        cart_recycler.setHasFixedSize(true);
        cart_recycler.setItemAnimator(new DefaultItemAnimator());
        // Set Adapter to Recycler
        adapter = new CartAdapter(ShoppingCartActivity.this, list);
        cart_recycler.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback item = new RecyclerTouchHelper(0, ItemTouchHelper.LEFT, ShoppingCartActivity.this);
        new ItemTouchHelper(item).attachToRecyclerView(cart_recycler);

        adapter.setOnCartItemClickListener(new RecyclerItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }
        });

    }

    private void getCartData() {
        total = 0.0;
        total_cart_val = 0.0;
        cart_progress.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CartProductsModel> call = serviceInterface.getCartProducts(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken());
        call.enqueue(new Callback<CartProductsModel>() {
            @Override
            public void onResponse(Call<CartProductsModel> call, Response<CartProductsModel> response) {
                CartProductsModel cartProductsModel = response.body();
                if (cartProductsModel.getStatus()) {
                    cart_recycler.setVisibility(View.VISIBLE);
                    cartNo_data_txtV.setVisibility(View.GONE);
                    checkout_btn.setEnabled(true);
                    list = cartProductsModel.getProducts();
                    cart_id = list.get(0).getCart_id();
                    Log.i(TAG, list.size() + "");
                    for (int i = 0; i < list.size(); i++) {
                        total += list.get(i).getPrice() * list.get(i).getQuentity();
                        Log.i("Total: ", total + "");
                    }

                    buildRecyclerView(list);
                    String totalAsString = String.format("%.2f", total);
                    items_price.setText(totalAsString);
                    items_price.append("$");
                    delivery_fee_txtV.setText(String.valueOf(delivery_fee_val));
                    delivery_fee_txtV.append("$");
                    total_cart_val = total + delivery_fee_val;
                    String total_cart_AsString = String.format("%.2f", total_cart_val);
                    total_cart_price_txtV.setText(total_cart_AsString);
                    total_cart_price_txtV.append("$");
                } else {
                    cart_recycler.setVisibility(View.GONE);
                    cartNo_data_txtV.setVisibility(View.VISIBLE);
                    checkout_btn.setEnabled(false);
//                    Toast.makeText(ShoppingCart.this, cartProductsModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
                cart_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CartProductsModel> call, Throwable t) {
                t.printStackTrace();
                cart_progress.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.cart_back_txtV_id)
    public void go_back() {
        finish();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getBrand_name_ar();

            // backup of removed item for undo purpose
            final CartProductsModel.Product deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            removeFromCart(ShoppingCartActivity.this, deletedItem.getProduct_id(), deletedItem.getCart_id(), MainPageActivity.userData.getToken());
            adapter.removeItem(viewHolder.getAdapterPosition());

            if (list.size() == 0) {
                cart_recycler.setVisibility(View.GONE);
                cartNo_data_txtV.setVisibility(View.VISIBLE);
            }
            // Update Total Price
            total -= deletedItem.getPrice() * deletedItem.getQuentity();
            items_price.setText(String.valueOf(total));
            items_price.append("$");

            total_cart_val -= deletedItem.getPrice() * deletedItem.getQuentity();
            total_cart_price_txtV.setText(String.valueOf(total_cart_val));
            total_cart_price_txtV.append("$");
        }
    }

    @OnClick(R.id.checkOut_btn_id)
    public void checkout_clicked() {
        check_list = new ArrayList<>();
        Log.i(TAG, CartAdapter.list.size() + "");

        for (int i = 0; i < CartAdapter.list.size(); i++) {
            check_list.add(new CheckOutListModel(CartAdapter.list.get(i).getProduct_id(), CartAdapter.list.get(i).getQuentity(), CartAdapter.list.get(i).getPrice()));
        }

        Gson gson = new Gson();
        String obj = gson.toJson(check_list);
        Log.i("check_list: ", check_list.size() + "");
        Log.i("check_list: ", obj + "");

        Intent intent = new Intent(ShoppingCartActivity.this, Checkout2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) check_list);
        bundle.putParcelableArrayList("show_list", (ArrayList<? extends Parcelable>) CartAdapter.list);
        bundle.putDouble("items_price", total);
        bundle.putDouble("delivery_fee", delivery_fee_val);
        bundle.putDouble("order_cost", total_cart_val);
        bundle.putInt("Cart_id", cart_id);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void removeFromCart(final Activity activity, int prod_id, int cart_id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.delateCartItem(prod_id, cart_id, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    MainPageActivity.cart_count--;
                    activity.invalidateOptionsMenu();
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
