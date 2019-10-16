package com.tomatoo.Main.CategoriesTabs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.CategoriesItems.ItemDetailsActivity;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.MenuActivities.Communications.AboutUsActivity;
import com.tomatoo.MenuActivities.Communications.ContactUsActivity;
import com.tomatoo.MenuActivities.Communications.HowToUseActivity;
import com.tomatoo.MenuActivities.CompareList.MyCompareListActivity;
import com.tomatoo.MenuActivities.Profile.ProfileActivity;
import com.tomatoo.MenuActivities.Settings.SettingActivity;
import com.tomatoo.MenuActivities.Orders.MyOrderActivity;
import com.tomatoo.MenuActivities.Notifications.NotificationsActivity;
import com.tomatoo.MenuActivities.WishList.MyWishListActivity;
import com.tomatoo.Models.Category;
import com.tomatoo.Models.CategoryDataModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.Models.FeaturedProductsModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerOnItemClickListner;
import com.tomatoo.utils.DealingWithCartItem;
import com.tomatoo.utils.DealingWithWishListItem;
import com.tomatoo.utils.EndlessRecyclerViewScrollListener;
import com.tomatoo.utils.PreferencesHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    FeaturedProductsAdapter products_adapter;

    @BindView(R.id.all_categories_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.categories_tabLayout_id)
    TabLayout categories_tablayout;
    @BindView(R.id.subCategories_tabLayout_id)
    TabLayout subCategories_tabLayout;
    @BindView(R.id.products_recycler_id)
    RecyclerView recyclerView;
    @BindView(R.id.no_available_products_txtV_id)
    TextView no_available_prods_txtV;

    private final String TAG = this.getClass().getSimpleName();
    private NetworkAvailable networkAvailable;

    private ArrayList<Category> categorylList;
    private ArrayList<Category> sub_CategorylList;
    private ArrayList<FeaturedProductsModel.ProductData> productslList;
    private int all_products_page = 1;
    private int last_cat_position = 0;
    private String type = "all";

    private int subCategory_page = 1;
    private int category_products_page = 1;

    DealingWithWishListItem dealingWithWishListItem;
    DealingWithCartItem dealingWithCartItem;
    private String sort_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        dealingWithWishListItem = new DealingWithWishListItem(CategoriesActivity.this);
        dealingWithCartItem = new DealingWithCartItem(CategoriesActivity.this);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        if (getIntent().hasExtra("sort_type")) {
            sort_type = getIntent().getStringExtra("sort_type");
        }
        networkAvailable = new NetworkAvailable(CategoriesActivity.this);
        categorylList = new ArrayList<>();
        productslList = new ArrayList<>();
        buildRecyclerView();
        if (networkAvailable.isNetworkAvailable())
            getAllCategoriesList(1);
        else
            Toast.makeText(CategoriesActivity.this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();


        categories_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBar.setVisibility(View.VISIBLE);
                last_cat_position = tab.getPosition();
                productslList.clear();
                if (tab.getPosition() == 0) {
                    all_products_page = 1;
                    subCategories_tabLayout.setVisibility(View.GONE);
                    getAllProducts(all_products_page, sort_type);
                } else {
                    checkForSubCategory(subCategory_page, categorylList.get(tab.getPosition()).getCategory_id());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        subCategories_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBar.setVisibility(View.VISIBLE);
                productslList.clear();
                Log.i("sub_pos:", tab.getPosition() + "");
                getSubCategoryProducts(sub_CategorylList.get(tab.getPosition()).getCategory_id());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getSubCategoryProducts(int subCat_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.subCategoryProducts(subCategory_page, 20, subCat_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkForSubCategory(int page, final int category_id) {
        sub_CategorylList = new ArrayList<>();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getSubCategory(page, 20, category_id);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    sub_CategorylList.addAll(response.body().getCategories());
                    subCategories_tabLayout.removeAllTabs();
                    subCategories_tabLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < sub_CategorylList.size(); i++) {
                        subCategories_tabLayout.addTab(
                                subCategories_tabLayout.newTab()
                                        .setText(sub_CategorylList.get(i).getCategory_name_en()));
                    }
                } else {
                    getCategoryProducts(category_id);
                    subCategories_tabLayout.removeAllTabs();
                    subCategories_tabLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCategoryProducts(int category_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getCategoryProducts(category_products_page, 20, category_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAllProducts(int all_products_page, String sort_type) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getAllProducts(all_products_page, 20, sort_type);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
//                    buildRecyclerView(productslList);
                    Log.i("cat_list", categorylList.size() + "");
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_prods_txtV.setVisibility(View.GONE);

//                    products_adapter.notifyItemRangeInserted(products_adapter.getItemCount(), productslList.size() - 1);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_available_prods_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_available_prods_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void getAllCategoriesList(int page) {
        categorylList = new ArrayList<>();
        categorylList.add(new Category(0, "All", "الكل", "All", ""));
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getAllCategory(page, 20);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    categorylList.addAll(response.body().getCategories());
                    for (int i = 0; i < categorylList.size(); i++) {
                        if (PreferencesHelper.getSomeStringValue(CategoriesActivity.this).equals("en")) {
                            categories_tablayout.addTab(
                                    categories_tablayout.newTab().setText(categorylList.get(i).getCategory_name_en()));
                        } else {
                            categories_tablayout.addTab(
                                    categories_tablayout.newTab().setText(categorylList.get(i).getCategory_name_ar()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void buildRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoriesActivity.this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        products_adapter = new FeaturedProductsAdapter(CategoriesActivity.this, productslList);
        recyclerView.setAdapter(products_adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (last_cat_position == 0) {
                    all_products_page++;
                    getAllProducts(all_products_page, sort_type);
                }
            }
        });

        products_adapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(CategoriesActivity.this, ItemDetailsActivity.class);
                intent.putExtra("prod_id", productslList.get(position).getProduct_id());
                if (PreferencesHelper.getSomeStringValue(CategoriesActivity.this).equals("ar")) {
                    intent.putExtra("prod_name", productslList.get(position).getProduct_name_ar());
                } else {
                    intent.putExtra("prod_name", productslList.get(position).getProduct_name_en());
                }
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (MainPageActivity.user_Id != 0) {
                    if (productslList.get(position).getCart() == 0) {
                        dealingWithCartItem.addTocart(CategoriesActivity.this, productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), MainPageActivity.userData.getId(), 1, MainPageActivity.userData.getToken());
                    } else {
                        dealingWithCartItem.removeFromCart(CategoriesActivity.this, productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), dealingWithCartItem.Cart_id, MainPageActivity.userData.getToken());
                    }
                } else {
                    startActivity(new Intent(CategoriesActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                if (MainPageActivity.user_Id != 0) {
                    if (productslList.get(position).getWishlists() == 0)
                        dealingWithWishListItem.addWishListItem(productslList.get(position), progressBar, wishlist_image);
                    else
                        removeWishListItem(productslList.get(position).getProduct_id(), position);
                } else {
                    startActivity(new Intent(CategoriesActivity.this, LoginActivity.class));
                }
            }
        });

    }

    private void removeWishListItem(int productId, final int position) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), productId);
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    productslList.get(position).setWishlists(0);
                    products_adapter.notifyDataSetChanged();
                    Toast.makeText(CategoriesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CategoriesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
