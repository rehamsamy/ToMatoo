package com.example.khaled_sa2r.tomatoo.MenuActivities.WishList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaled_sa2r.tomatoo.Main.CategoriesItems.CategoryItemDetailsActivity;
import com.example.khaled_sa2r.tomatoo.ItemModel;
import com.example.khaled_sa2r.tomatoo.R;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.Holder>{

    Context mContext;
    OnItemInterface mOnItem;
    List<ItemModel> itemList;



    public interface OnItemInterface{
        public  void onItemClick(int position);
    }

    public WishlistAdapter(Context mContext,OnItemInterface mOnItem) {
        this.mContext = mContext;
        this.mOnItem=mOnItem;

    }

    @NonNull
    @Override
    public WishlistAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.all_fragment_list_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        //        ItemModel model=itemList.get(i);
//       TextView name=(TextView) holder.itemView.findViewById(R.id.apple_name_txt);
//       name.setText(model.getName());
//      TextView price=  (TextView) holder.itemView.findViewById(R.id.apple_price_txt);
//      price.setText(model.getPrice());
        mOnItem.onItemClick(i);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, CategoryItemDetailsActivity.class));
            }
        });
    }



    @Override
    public int getItemCount() {
        return 4;
    }

    public class Holder  extends RecyclerView.ViewHolder{

        public Holder(@NonNull View itemView) {
            super(itemView);

        }


    }
}
