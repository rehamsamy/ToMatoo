package com.tomatoo.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.LogainAndRegistration.LoginActivity;
import com.tomatoo.Main.Adapters.MainCategoriesAdapter;
import com.tomatoo.Main.Adapters.MainFeaturedAdapter;
import com.tomatoo.Main.CategoriesItems.ItemDetailsActivity;
import com.tomatoo.Main.CategoriesItems.ShoppingCartActivity;
import com.tomatoo.Main.CategoriesTabs.CategoriesActivity;
import com.tomatoo.MenuActivities.Communications.AboutUsActivity;
import com.tomatoo.MenuActivities.Communications.ContactUsActivity;
import com.tomatoo.MenuActivities.Communications.HowToUseActivity;
import com.tomatoo.MenuActivities.CompareList.MyCompareListActivity;
import com.tomatoo.MenuActivities.Profile.ProfileActivity;
import com.tomatoo.MenuActivities.Settings.SettingActivity;
import com.tomatoo.MenuActivities.WishList.MyWishListActivity;
import com.tomatoo.Models.Category;
import com.tomatoo.Models.CategoryDataModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.Models.FeaturedProductsModel;
import com.tomatoo.Models.SliderModel;
import com.tomatoo.Models.UserModel;
import com.tomatoo.MyBase.MyBaseActivity;
import com.tomatoo.MenuActivities.Orders.MyOrderActivity;
import com.tomatoo.MenuActivities.Notifications.NotificationsActivity;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerOnItemClickListner;
import com.tomatoo.utils.DealingWithCartItem;
import com.tomatoo.utils.DealingWithWishListItem;
import com.tomatoo.utils.EndlessRecyclerViewScrollListener;
import com.tomatoo.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tomatoo.LogainAndRegistration.LoginActivity.MY_PREFS_NAME;

public class MainPageActivity extends MyBaseActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.nv)
    NavigationView navigationView;
    @BindView(R.id.banner_slider)
    SliderLayout sliderLayout;
    @BindView(R.id.mainCategories_progress_id)
    ProgressBar categories_progress;
    @BindView(R.id.mainFeatured_progress_id)
    ProgressBar featured_progress;
    @BindView(R.id.main_categories_recyclerV_id)
    RecyclerView categories_recyclerV;
    @BindView(R.id.featured_products_recyclerV_id)
    RecyclerView featuredProducts_recyclerV;
    @BindView(R.id.log_out_sidemenu)
    TextView log_out_sidemenu;

    private NetworkAvailable networkAvailable;
    public static UserModel userData;
    public static int cart_count = 0;
    private String sort_type = "lp";
    DrawerLayout drawer;
    TextView header_title, header_back_txtV, header_back_txtV2;
    ImageView nav_header_img;
    private List<SliderModel.Slider> images_list;
    private DealingWithCartItem dealingWithCartItem;
    private DealingWithWishListItem dealingWithWishListItem;

    //Featured Products
    List<FeaturedProductsModel.ProductData> featuredProductsList;
    List<FeaturedProductsModel.ProductData> newSortedList;
    MainFeaturedAdapter mainFeaturedAdapter;
    ArrayList<String> layouts = new ArrayList<>();
    Dialog myDialog;
    // Categories
    private List<Category> categoriesList;
    MainCategoriesAdapter categoriesAdapter;

    private int selected_filterItem = 0;
    private int category_current_page = 1;
    private int featured_page = 1;
    private int featured_limit = 10;
    public static int user_Id = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        myDialog = new Dialog(this);
        networkAvailable = new NetworkAvailable(this);
        Toolbar toolbar = findViewById(R.id.mainPage_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        categories_progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        featured_progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        // Set UserData to Navigation
        View hView = navigationView.getHeaderView(0);
        header_title = hView.findViewById(R.id.nav_username);
        nav_header_img = hView.findViewById(R.id.nav_userImage);

        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getParcelableExtra("user_data");
            user_Id = userData.getId();
            header_title.setText(userData.getName());
            Glide.with(MainPageActivity.this)
                    .load(userData.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.sidemenu_user_pic)
                    .into(nav_header_img);
        }

        categoriesList = new ArrayList<>();
        featuredProductsList = new ArrayList<>();
        dealingWithCartItem = new DealingWithCartItem(MainPageActivity.this);
        dealingWithWishListItem = new DealingWithWishListItem(MainPageActivity.this);

        if (user_Id == 0) {
            log_out_sidemenu.setVisibility(View.GONE);
        }

        buildCategoriesRecycler();
        buildFeaturesProdsRecycler(featuredProductsList);

        if (networkAvailable.isNetworkAvailable()) {
            getSliderImages();
            getCategoriesList(category_current_page);
            createFeaturedProdList(featured_page);
        } else
            Toast.makeText(activity, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.see_all_txtV)
    void seeAllProducts() {
        Intent intent = new Intent(MainPageActivity.this, CategoriesActivity.class);
        intent.putExtra("sort_type", sort_type);
        startActivity(intent);
    }

    @OnClick(R.id.log_out_sidemenu)
    void setLogOut() {
        drawer.closeDrawers();
        logoutOfApp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainPageActivity.this, cart_count, R.drawable.ic_shopping_cart_black_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_action) {
            if (user_Id != 0) {
                Intent intent = new Intent(MainPageActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            } else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
        } else if (id == R.id.filter_action) {
            show_popup();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, ProfileActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_cart) {
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, ShoppingCartActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_wishlist) {
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, MyWishListActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_comparelist) {
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, MyCompareListActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_orders) {
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, MyOrderActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_notification) {
            if (user_Id != 0)
                startActivity(new Intent(MainPageActivity.this, NotificationsActivity.class));
            else
                startActivity(new Intent(MainPageActivity.this, LoginActivity.class));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainPageActivity.this, SettingActivity.class));

        } else if (id == R.id.nav_aboutapp) {
            startActivity(new Intent(MainPageActivity.this, AboutUsActivity.class));


        } else if (id == R.id.nav_how_to_use) {
            startActivity(new Intent(MainPageActivity.this, HowToUseActivity.class));

        } else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(MainPageActivity.this, ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getSliderImages() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SliderModel> call = serviceInterface.getSlider();
        call.enqueue(new Callback<SliderModel>() {
            @Override
            public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                SliderModel sliderModel = response.body();
                if (sliderModel.getStatus()) {
                    images_list = sliderModel.getSlider();
                    for (int i = 0; i < images_list.size(); i++) {
                        TextSliderView textSliderView = new TextSliderView(MainPageActivity.this);
                        // initialize a SliderLayout
                        textSliderView.image(images_list.get(i).getSlider_image())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(MainPageActivity.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        sliderLayout.addSlider(textSliderView);

                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(4000);
                    sliderLayout.addOnPageChangeListener(MainPageActivity.this);
                }
            }

            @Override
            public void onFailure(Call<SliderModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCategoriesList(int category_current_page) {
        categories_progress.setVisibility(View.VISIBLE);
        // Bind Data For Categories
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getMainCategory(category_current_page, 10);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    categoriesList.addAll(response.body().getCategories());
                    categoriesAdapter.notifyDataSetChanged();
                }
                categories_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
                categories_progress.setVisibility(View.GONE);
            }
        });
    }

    private void buildCategoriesRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // For Categories RecyclerView
        categories_recyclerV.setLayoutManager(linearLayoutManager);
        categories_recyclerV.setHasFixedSize(true);
        categories_recyclerV.getLayoutManager().scrollToPosition(0);
        // Categories
        categoriesAdapter = new MainCategoriesAdapter(MainPageActivity.this, categoriesList);
        categories_recyclerV.setAdapter(categoriesAdapter);
        categories_recyclerV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                category_current_page++;
                getCategoriesList(category_current_page);
            }
        });


    }

    private void createFeaturedProdList(int featured_page) {
        featured_progress.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getFeaturedProducts(featured_page, featured_limit, 2);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                FeaturedProductsModel featuredProductsModel = response.body();
                if (response.body().getStatus()) {
                    featuredProductsList.addAll(response.body().getProducts());
                    mainFeaturedAdapter.notifyDataSetChanged();
                }
                featured_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                featured_progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void buildFeaturesProdsRecycler(List<FeaturedProductsModel.ProductData> featuredProductsList) {
        // For Featured  RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        featuredProducts_recyclerV.setLayoutManager(linearLayoutManager);
        featuredProducts_recyclerV.setHasFixedSize(true);
        featuredProducts_recyclerV.getLayoutManager().scrollToPosition(0);
        // Set Adapter ...
        mainFeaturedAdapter = new MainFeaturedAdapter(MainPageActivity.this, featuredProductsList);
        featuredProducts_recyclerV.setAdapter(mainFeaturedAdapter);

        featuredProducts_recyclerV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                featured_page++;
                createFeaturedProdList(featured_page);
            }
        });

        mainFeaturedAdapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainPageActivity.this, ItemDetailsActivity.class);
                intent.putExtra("prod_id", MainPageActivity.this.featuredProductsList.get(position).getProduct_id());
                if (PreferencesHelper.getSomeStringValue(MainPageActivity.this).equals("ar")) {
                    intent.putExtra("prod_name", MainPageActivity.this.featuredProductsList.get(position).getProduct_name_ar());
                    intent.putExtra("prod_category", MainPageActivity.this.featuredProductsList.get(position).getCategory_name_ar());
                } else {
                    intent.putExtra("prod_name", MainPageActivity.this.featuredProductsList.get(position).getProduct_name_en());
                    intent.putExtra("prod_category", MainPageActivity.this.featuredProductsList.get(position).getCategory_name_en());
                }
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (user_Id != 0) {
                    Log.i("cart: ", MainPageActivity.this.featuredProductsList.get(position).getCart() + "");
                    if (MainPageActivity.this.featuredProductsList.get(position).getCart() == 0) {
                        dealingWithCartItem.addTocart(MainPageActivity.this, MainPageActivity.this.featuredProductsList.get(position), featured_progress, cart_image, MainPageActivity.this.featuredProductsList.get(position).getProduct_id(), MainPageActivity.userData.getId(), 1, MainPageActivity.userData.getToken());
                    } else {
                        dealingWithCartItem.removeFromCart(MainPageActivity.this, MainPageActivity.this.featuredProductsList.get(position), featured_progress, cart_image, MainPageActivity.this.featuredProductsList.get(position).getProduct_id(), dealingWithCartItem.Cart_id, MainPageActivity.userData.getToken());
                    }
                } else {
                    startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                if (user_Id != 0) {
                    if (MainPageActivity.this.featuredProductsList.get(position).getWishlists() == 0) {
                        dealingWithWishListItem.addWishListItem(MainPageActivity.this.featuredProductsList.get(position), featured_progress, wishlist_image);
                    } else {
                        removeWishListItem(MainPageActivity.this.featuredProductsList.get(position), position);
                    }
                } else {
                    startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void removeWishListItem(final FeaturedProductsModel.ProductData item_model, final int position) {
        featured_progress.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), item_model.getProduct_id());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    item_model.setWishlists(0);
                    mainFeaturedAdapter.notifyDataSetChanged();
                    Toast.makeText(MainPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).
                            show();
                } else {
                }
                featured_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
                featured_progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    private void logoutOfApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
        builder.setMessage(getString(R.string.outofApp))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //super.onBackPressed();
                        dialogInterface.dismiss();
                        //-------------------------------------------------------
                        pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
                        finish();
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

    private void show_popup() {
        final RadioGroup price_radioGroup, rate_radioGroup;
        RadioButton high_price_rb, low_price_rb, high_rate_rb, low_rate_rb;
        Button done_btn;
        TextView userName_txtV, serviceType_txtV, completedServices_txtV, timeToArrive_txtV;
        RatingBar ratingBar;
        final boolean[] isChecking = {true};
        final int[] mCheckedId = {R.id.sort_high_price};

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.sort_filter_popup_layout);
        price_radioGroup = myDialog.findViewById(R.id.price_sorting_rg);
        rate_radioGroup = myDialog.findViewById(R.id.rate_radioGroup_id);
        high_price_rb = myDialog.findViewById(R.id.sort_high_price);
        low_price_rb = myDialog.findViewById(R.id.sort_low_price);
        high_rate_rb = myDialog.findViewById(R.id.sort_high_rate);
        low_rate_rb = myDialog.findViewById(R.id.sort_low_rate);
        done_btn = myDialog.findViewById(R.id.sorting_done_btn_id);

        price_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking[0]) {
                    isChecking[0] = false;
                    rate_radioGroup.clearCheck();
                    mCheckedId[0] = checkedId;
                }
                isChecking[0] = true;
                if (checkedId == R.id.sort_low_price) {
                    selected_filterItem = 1;
                    sort_type = "lp";
//                    buildFeaturesProdsRecycler(lowPriceSort(featuredProductsList));
                } else if (checkedId == R.id.sort_high_price) {
                    selected_filterItem = 2;
                    sort_type = "hp";
                }
            }
        });

        rate_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking[0]) {
                    isChecking[0] = false;
                    price_radioGroup.clearCheck();
                    mCheckedId[0] = checkedId;
                }
                isChecking[0] = true;
                if (checkedId == R.id.sort_low_rate) {
                    selected_filterItem = 3;
                    sort_type = "lr";
                } else if (checkedId == R.id.sort_high_rate) {
                    selected_filterItem = 4;
                    sort_type = "hr";
                }
            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public List<FeaturedProductsModel.ProductData> lowPriceSort(List<FeaturedProductsModel.ProductData> input) {
        newSortedList = new ArrayList<>();

        for (int i = 1; i < input.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (Integer.parseInt(input.get(i).getPrice()) < Integer.parseInt(input.get(j - 1).getPrice())) {
                    newSortedList.add(input.get(j));
                }
            }
        }
        return input;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
