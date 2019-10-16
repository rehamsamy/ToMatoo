package com.tomatoo.MenuActivities.CompareList;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.ApiException;
import com.squareup.picasso.Picasso;
import com.tomatoo.Connection.ApiClient;
import com.tomatoo.Connection.ApiServiceInterface;
import com.tomatoo.Main.MainPageActivity;
import com.tomatoo.Models.AddToCartResponseModel;
import com.tomatoo.Models.CompareListModel;
import com.tomatoo.Models.DelOrAddWishlistModel;
import com.tomatoo.R;
import com.tomatoo.utils.Urls;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompareListAdapter extends RecyclerView.Adapter<CompareListAdapter.ViewHolder> {

    private Context mContext;
    private List<CompareListModel.CompareProduct> list;
    private int cart_id = 0;

    public CompareListAdapter(Context mContext, List<CompareListModel.CompareProduct> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public CompareListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(com.tomatoo.R.layout.compare_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompareListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.name_txtV.setText(list.get(position).getProduct_name_en());
        viewHolder.category_txtV.setText(list.get(position).getCategory_name_en());
        viewHolder.price_txtV.setText(list.get(position).getPrice() + " $");
        Glide.with(mContext)
                .load(Urls.BASE_URL + list.get(position).getPhoto())
                .centerCrop()
                .into(viewHolder.item_main_imageV);

        if (list.get(position).getWishlists() == 1)
            viewHolder.favorite_imageV.setImageResource(R.drawable.wishlist_select);
        else
            viewHolder.favorite_imageV.setImageResource(R.drawable.wishlist_not_select);

        if (list.get(position).getCart() == 1)
            viewHolder.cart_imageV.setImageResource(R.drawable.cart_select);
        else
            viewHolder.cart_imageV.setImageResource(R.drawable.cart_not_select);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.compareItem_imageV_id)
        ImageView item_main_imageV;
        @BindView(R.id.item_category_txtV)
        TextView category_txtV;
        @BindView(R.id.item_name_txtV)
        TextView name_txtV;
        @BindView(R.id.item_price_txtV)
        TextView price_txtV;
        @BindView(R.id.compareItem_rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.favorite_imageV_id)
        ImageView favorite_imageV;
        @BindView(R.id.cart_imageV_id)
        ImageView cart_imageV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cart_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("cart: ", list.get(getAdapterPosition()).getCart() + "");
                    if (list.get(getAdapterPosition()).getCart() == 0) {
                        addToCart((Activity) mContext, list.get(getAdapterPosition()), getAdapterPosition(), cart_imageV);
                    } else {
                        removeFromCart((Activity) mContext, list.get(getAdapterPosition()), getAdapterPosition(), cart_imageV);
                    }
                }
            });

            favorite_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("cart: ", list.get(getAdapterPosition()).getWishlists() + "");
                    if (list.get(getAdapterPosition()).getCart() == 0) {
                        addToWishlist((Activity) mContext, list.get(getAdapterPosition()), getAdapterPosition(), favorite_imageV);
                    } else {
                        removeFromWishList((Activity) mContext, list.get(getAdapterPosition()), getAdapterPosition(), favorite_imageV);
                    }
                }
            });
        }
    }

    private void removeFromWishList(final Activity mContext, final CompareListModel.CompareProduct compareProduct, final int adapterPosition, ImageView favorite_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainPageActivity.userData.getId(), MainPageActivity.userData.getToken(), compareProduct.getProduct_id());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    compareProduct.setWishlists(0);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addToWishlist(final Activity mContext, final CompareListModel.CompareProduct compareProduct, int adapterPosition, final ImageView favorite_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.addToWishList(MainPageActivity.userData.getId(), compareProduct.getProduct_id(), MainPageActivity.userData.getToken());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    compareProduct.setWishlists(1);
                    favorite_imageV.setImageResource(R.drawable.wishlist_select);
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void removeFromCart(final Activity activity, final CompareListModel.CompareProduct compareProduct, int adapterPosition, final ImageView cart_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.delateCartItem(compareProduct.getProduct_id(), cart_id, MainPageActivity.userData.getToken());
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    compareProduct.setCart(0);
                    MainPageActivity.cart_count--;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_not_select);
                    Toast.makeText(mContext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addToCart(final Activity activity, final CompareListModel.CompareProduct compareProduct, int adapterPosition, final ImageView cart_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(compareProduct.getProduct_id(), MainPageActivity.user_Id, 1, MainPageActivity.userData.getToken());
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    cart_id = addToCartResponseModel.getCart_id();
                    compareProduct.setCart(1);
                    MainPageActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_select);
                    Toast.makeText(CompareListAdapter.this.mContext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CompareListAdapter.this.mContext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}
