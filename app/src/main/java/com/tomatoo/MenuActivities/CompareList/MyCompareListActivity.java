package com.tomatoo.MenuActivities.CompareList;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Connection.NetworkAvailable;
import com.tomatoo.Main.CategoriesItems.CartAdapter;
import com.tomatoo.Main.CategoriesItems.ItemDetailsActivity;
import com.tomatoo.Main.CategoriesItems.ShoppingCartActivity;
import com.tomatoo.Main.CategoriesTabs.FeaturedProductsAdapter;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.MenuActivities.WishList.MyWishListActivity;
import com.tomatoo.MenuActivities.WishList.WishlistAdapter;
import com.tomatoo.Models.AddToCartResponseModel;
import com.tomatoo.Models.CartProductsModel;
import com.tomatoo.Models.CompareListModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.Models.DeleteCartModel;
import com.tomatoo.Models.FeaturedProductsModel;
import com.tomatoo.Models.WishListModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerOnItemClickListner;
import com.tomatoo.interfaces.RecyclerTouchHelper;
import com.tomatoo.interfaces.RecyclerTouchHelperListner;
import com.tomatoo.utils.DealingWithCartItem;
import com.tomatoo.utils.DealingWithWishListItem;
import com.tomatoo.utils.DialogUtil;
import com.tomatoo.utils.EndlessRecyclerViewScrollListener;
import com.tomatoo.utils.PreferencesHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCompareListActivity extends AppCompatActivity implements RecyclerTouchHelperListner {

    @BindView(R.id.compareList_recyclerV_id)
    RecyclerView compareList_recyclerV;
    @BindView(R.id.compareList_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.compareList_no_data_txtV)
    TextView no_data_txtV;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private DealingWithCartItem dealingWithCartItem;
    private DealingWithWishListItem dealingWithWishListItem;
    private CompareListAdapter adapter;
    private List<CompareListModel.CompareProduct> comapre_List;

    // Vars...
    private int current_Page = 1;
    private int limit = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_compare_list);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        dealingWithCartItem = new DealingWithCartItem(MyCompareListActivity.this);
        dealingWithWishListItem = new DealingWithWishListItem(MyCompareListActivity.this);

        if (networkAvailable.isNetworkAvailable()) {
            getCompareList(MainPageActivity.user_Id, current_Page);
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getCompareList(int user_Id, int current_Page) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CompareListModel> call = serviceInterface.compareListProducts(user_Id, current_Page, limit, MainPageActivity.userData.getToken());
        call.enqueue(new Callback<CompareListModel>() {
            @Override
            public void onResponse(Call<CompareListModel> call, Response<CompareListModel> response) {
                CompareListModel compareListModel = response.body();
                if (compareListModel.getStatus()) {
                    compareList_recyclerV.setVisibility(View.VISIBLE);
                    no_data_txtV.setVisibility(View.GONE);

                    comapre_List = compareListModel.getProducts();
                    buildWishlistRecycler(comapre_List);
                } else {
                    compareList_recyclerV.setVisibility(View.GONE);
                    no_data_txtV.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CompareListModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void buildWishlistRecycler(final List<CompareListModel.CompareProduct> list) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        compareList_recyclerV.setLayoutManager(gridLayoutManager);
        compareList_recyclerV.setHasFixedSize(true);
        compareList_recyclerV.setItemAnimator(new DefaultItemAnimator());

        adapter = new CompareListAdapter(MyCompareListActivity.this, list);
        // Set Data to ArrayList And to Recycler ...
        compareList_recyclerV.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback item = new RecyclerTouchHelper(0, ItemTouchHelper.LEFT, MyCompareListActivity.this);
        new ItemTouchHelper(item).attachToRecyclerView(compareList_recyclerV);

        compareList_recyclerV.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                current_Page++;
                getCompareList(MainPageActivity.user_Id, current_Page);
            }
        });
    }

    @OnClick(R.id.compareList_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.compareList_cart_txtV_id)
    void goToCart() {
        Intent intent = new Intent(MyCompareListActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = comapre_List.get(viewHolder.getAdapterPosition()).getProduct_name_ar();

            // backup of removed item for undo purpose
            final CompareListModel.CompareProduct deletedItem = comapre_List.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            removeCompareListItem(deletedItem.getProduct_id(), MainPageActivity.userData.getToken());
            adapter.removeItem(viewHolder.getAdapterPosition());

            if (comapre_List.size() == 0) {
                compareList_recyclerV.setVisibility(View.GONE);
                no_data_txtV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void removeCompareListItem(int product_id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeleteCartModel> call = serviceInterface.deleteCompareListItem(MainPageActivity.user_Id, token, product_id);
        call.enqueue(new Callback<DeleteCartModel>() {
            @Override
            public void onResponse(Call<DeleteCartModel> call, Response<DeleteCartModel> response) {
                DeleteCartModel deleteCartModel = response.body();
                if (deleteCartModel.getStatus()) {
                    Toast.makeText(MyCompareListActivity.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyCompareListActivity.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
