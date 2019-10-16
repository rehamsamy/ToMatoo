package com.tomatoo.MenuActivities.WishList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.ItemModel;
import com.tomatoo.Main.CategoriesItems.ItemDetailsActivity;
import com.tomatoo.Main.CategoriesItems.ShoppingCartActivity;
import com.tomatoo.Main.CategoriesTabs.AllFragmentAdapter;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.AddToCartResponseModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.Models.WishListModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerOnItemClickListner;
import com.tomatoo.utils.DealingWithCartItem;
import com.tomatoo.utils.DealingWithWishListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWishListActivity extends AppCompatActivity {

    @BindView(R.id.wishlist_back_txtV_id)
    ImageView wishlist_back;
    @BindView(R.id.wishlist_cart_txtV_id)
    TextView wishlist_cart_icon;
    @BindView(R.id.wishlist_recyclerV_id)
    RecyclerView wishlist_recyclerV;
    @BindView(R.id.wishlist_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.wishList_no_data_txtV)
    TextView wishList_no_data_txtV;

    private int current_page = 1;
    private int page_limit = 20;
    ArrayList<WishListModel.WishlistItem> list;
    WishlistAdapter adapter;
    private NetworkAvailable networkAvailable;
    private DealingWithCartItem dealingWithCartItem;
    private DealingWithWishListItem dealingWithWishListItem;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dealingWithWishListItem = new DealingWithWishListItem(this);
        dealingWithCartItem = new DealingWithCartItem(this);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        if (networkAvailable.isNetworkAvailable()) {
            getWishList(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), current_page, page_limit);
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildWishlistRecycler(final List<WishListModel.WishlistItem> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        wishlist_recyclerV.setLayoutManager(gridLayoutManager);
        wishlist_recyclerV.setHasFixedSize(true);
        wishlist_recyclerV.setItemAnimator(new DefaultItemAnimator());

        // Set Data to ArrayList And to Recycler ...
        adapter = new WishlistAdapter(MyWishListActivity.this, list);
        wishlist_recyclerV.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                // Item Clicked ...
                WishListModel.WishlistItem itemModel = list.get(position);
                Intent intent = new Intent(MyWishListActivity.this, ItemDetailsActivity.class);
                intent.putExtra("prod_id", itemModel.getProduct_id());
                // Check For Lang First ...
                intent.putExtra("prod_name", itemModel.getProduct_name_en());
                intent.putExtra("prod_category", itemModel.getCategory_name_en());
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (list.get(position).getCart() == 0) {
                    addTocart(MyWishListActivity.this, list.get(position), progressBar, cart_image, MainPageActivity.userData.getId(), 1, MainPageActivity.userData.getToken());
                } else {
                    removeFromCart(MyWishListActivity.this, list.get(position), progressBar, cart_image, 1, MainPageActivity.userData.getToken());
                }
            }

            @Override
            public void OnWishListClick(final int position, ImageView wishlist_image) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWishListActivity.this);
                builder.setMessage(getString(R.string.delete_this_Item))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //super.onBackPressed();
                                dialogInterface.dismiss();
                                //-------------------------------------------------------
                                deleteWishListItem(list, adapter, position, MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), list.get(position).getProduct_id(), progressBar);
                                // -------------------------------------------------------
                            }
                        })
                        .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    @OnClick(R.id.wishlist_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.wishlist_cart_txtV_id)
    public void cart_go() {
        Intent intent = new Intent(MyWishListActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
        finish();
    }

    private void getWishList(int id, String token, int current_page, int page_limit) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<WishListModel> call = serviceInterface.getWishListData(id, token, current_page, page_limit);
        call.enqueue(new Callback<WishListModel>() {
            @Override
            public void onResponse(Call<WishListModel> call, Response<WishListModel> response) {
                WishListModel wishListModel = response.body();
                if (response.body().getStatus()) {
                    wishlist_recyclerV.setVisibility(View.VISIBLE);
                    wishList_no_data_txtV.setVisibility(View.GONE);

                    buildWishlistRecycler(wishListModel.getProducts());

                } else {
                    wishlist_recyclerV.setVisibility(View.GONE);
                    wishList_no_data_txtV.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<WishListModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeItem(int position) {
        list.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void addTocart(final Activity activity, final WishListModel.WishlistItem item_model, final ProgressBar progressBar, final ImageView cart_imageV, int user_id, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(item_model.getProduct_id(), user_id, qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    item_model.setCart(1);
                    MainPageActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_select);
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeFromCart(final Activity activity, final WishListModel.WishlistItem item_model, final ProgressBar progressBar, final ImageView cart_imageV, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.delateCartItem(item_model.getProduct_id(), qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    item_model.setCart(0);
                    MainPageActivity.cart_count--;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_not_select);
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void deleteWishListItem(final List<WishListModel.WishlistItem> list, final WishlistAdapter adapter, final int position, final int id, String token, int productId, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(id, token, productId);
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    removeItem(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
