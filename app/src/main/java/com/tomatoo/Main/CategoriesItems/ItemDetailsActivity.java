package com.tomatoo.Main.CategoriesItems;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.CategoriesTabs.CategoriesActivity;
import com.tomatoo.Main.Converter;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.AddToCartResponseModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.Models.ProductDetailsModel;
import com.tomatoo.R;
import com.tomatoo.utils.DealingWithWishListItem;
import com.tomatoo.utils.PreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {

    @BindView(R.id.item_main_imageV_id)
    ImageView item_Image;
    @BindView(R.id.add_to_wishlist_img_id)
    ImageView wishlist_image;
    @BindView(R.id.item_Cart_imageV_id)
    ImageView cart_image;
    @BindView(R.id.minus_item_count_img_id)
    ImageView minus_item_count;
    @BindView(R.id.plus_item_count_img_id)
    ImageView plus_item_count;
    @BindView(R.id.item_name_txtV_id)
    TextView item_name;
    @BindView(R.id.item_price_txtV_id)
    TextView item_unit_price;
    @BindView(R.id.item_category_txtV_id)
    TextView item_category;
    @BindView(R.id.item_description_txtV_id)
    TextView item_description;
    @BindView(R.id.item_total_price_txtV_id)
    TextView item_total_price;
    @BindView(R.id.item_count_txtV_id)
    TextView itemCount_txtV;
    @BindView(R.id.details_addToCart_btn_id)
    Button addToCart_btn;
    @BindView(R.id.prod_Details_layout_id)
    ConstraintLayout prodDetails_layout;
    @BindView(R.id.details_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.item_rating_bar)
    RatingBar item_rating_bar;
    @BindView(R.id.itemDetails_name_toolbar)
    TextView itemDetails_name_toolbar;

    Toolbar toolbar;

    private int prod_id;
    private String prod_name, prod_cat;
    private String TAG = this.getClass().getSimpleName();
    ProductDetailsModel.ItemDetail itemDetail;
    ProductDetailsModel.ItemImages itemImage;
    private DealingWithWishListItem dealingWithWishListItem;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_details);
        ButterKnife.bind(this);

        dealingWithWishListItem = new DealingWithWishListItem(this);

        if (getIntent() != null) {
            prod_id = getIntent().getExtras().getInt("prod_id");
            prod_name = getIntent().getExtras().getString("prod_name");
            prod_cat = getIntent().getExtras().getString("prod_category");
            Log.i(TAG, prod_id + "");
            Log.i(TAG, prod_name + "");
            Log.i(TAG, PreferencesHelper.getSomeStringValue(ItemDetailsActivity.this));
            itemDetails_name_toolbar.setText(prod_name);
        }
//        setUpToolBar(prod_name);
        getProductDetails(prod_id, 0);
    }

    private void getProductDetails(int product_id, int user_id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ProductDetailsModel> call = serviceInterface.productDetails(product_id, user_id);
        call.enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
                ProductDetailsModel productModel = response.body();
                if (response.body().getStatus()) {
                    prodDetails_layout.setVisibility(View.VISIBLE);
                    itemDetail = productModel.getProducts().getDetails();
                    itemImage = productModel.getProducts().getImages();
                    // Set Data To Views
                    if (PreferencesHelper.getSomeStringValue(ItemDetailsActivity.this).equals("ar")) {
                        item_name.setText(itemDetail.getProduct_name_ar());
                        item_unit_price.setText(String.valueOf(itemDetail.getPrice()));
                        item_unit_price.append("$");
                        item_total_price.setText(String.valueOf(Integer.parseInt(itemCount_txtV.getText().toString()) * itemDetail.getPrice()));
                        item_total_price.append("$");
                        item_category.setText(prod_cat);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_ar(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_ar()));
                        }
                    } else {
                        item_name.setText(itemDetail.getProduct_name_en());
                        item_unit_price.setText(String.valueOf(itemDetail.getPrice()));
                        item_unit_price.append("$");
                        item_total_price.setText(String.valueOf(Integer.parseInt(itemCount_txtV.getText().toString()) * itemDetail.getPrice()));
                        item_total_price.append("$");
                        item_category.setText(prod_cat);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_en(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_en()));
                        }
                        prodDetails_layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    // Load Product Image
                    Glide.with(ItemDetailsActivity.this)
                            .load(itemImage.getProduct_image())
                            .fitCenter()
                            .into(item_Image);

                    Log.i("wishh: ", itemDetail.getWishlists() + "");
                    if (itemDetail.getWishlists() == 0) {
                        wishlist_image.setImageResource(R.drawable.wishlist_not_select);
                    } else {
                        wishlist_image.setImageResource(R.drawable.wishlist_select);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpToolBar(String itemName) {
        toolbar = (Toolbar) findViewById(R.id.itemDetails_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView textView = findViewById(R.id.itemDetails_name_toolbar);
        textView.setText(itemName);
    }

    @OnClick({R.id.details_addToCart_btn_id})
    public void addToCart() {
        if (MainPageActivity.user_Id != 0) {
            Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            addTocart(ItemDetailsActivity.this, itemDetail.getProduct_id(), MainPageActivity.userData.getId(), Integer.parseInt(itemCount_txtV.getText().toString()), MainPageActivity.userData.getToken());
        } else
            startActivity(new Intent(ItemDetailsActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.itemDetails_back_img)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.add_to_wishlist_img_id)
    public void addToWishlist() {
        if (MainPageActivity.user_Id != 0) {
            Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            if (itemDetail.getWishlists() == 0) {
                addWishListItem(itemDetail, wishlist_image);
            } else {
                removeWishListItem(itemDetail, wishlist_image);
            }
        } else
            startActivity(new Intent(ItemDetailsActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.plus_item_count_img_id)
    public void plusCount() {
        int item_count = Integer.parseInt(itemCount_txtV.getText().toString());
        Log.i(" count", item_count + "");
        item_count++;
        Log.i(" count", item_count + "");
        float total_price = item_count * itemDetail.getPrice();
        itemCount_txtV.setText(String.valueOf(item_count));
        item_total_price.setText(String.valueOf(total_price));
        item_total_price.append("$");
    }

    @OnClick(R.id.minus_item_count_img_id)
    public void minusCount() {
        int item_count = Integer.parseInt(itemCount_txtV.getText().toString());
        if (item_count > 1) {
            Log.i(" count", item_count + "");
            item_count--;
            Log.i(" count", item_count + "");
            float total_price = item_count * itemDetail.getPrice();
            itemCount_txtV.setText(String.valueOf(item_count));
            itemCount_txtV.setText(String.valueOf(item_count));
            item_total_price.setText(String.valueOf(total_price));
            item_total_price.append("$");
        }
    }

    @OnClick(R.id.itemDetails_cart_img)
    void goToCart() {
        if (MainPageActivity.user_Id != 0) {
            Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ItemDetailsActivity.this, ShoppingCartActivity.class);
            startActivity(intent);
        } else
            startActivity(new Intent(ItemDetailsActivity.this, LoginActivity.class));
    }

    // For Wishlist....
    private void addWishListItem(final ProductDetailsModel.ItemDetail item_model, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.addToWishList(MainPageActivity.userData.getId(), item_model.getProduct_id(), MainPageActivity.userData.getToken());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    item_model.setWishlists(1);
                    imageView.setImageResource(R.drawable.wishlist_select);
                    Toast.makeText(ItemDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ItemDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void removeWishListItem(final ProductDetailsModel.ItemDetail itemDetail, final ImageView wishlist_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), itemDetail.getProduct_id());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    itemDetail.setWishlists(0);
                    wishlist_imageV.setImageResource(R.drawable.wishlist_not_select);
                    Toast.makeText(ItemDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ItemDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    // For Cart ....
    private void addTocart(final Activity activity, int prod_id, int user_id, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(prod_id, user_id, qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    MainPageActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_image.setImageResource(R.drawable.cart_select);
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
}
