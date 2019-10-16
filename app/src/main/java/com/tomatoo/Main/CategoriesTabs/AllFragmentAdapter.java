package com.tomatoo.Main.CategoriesTabs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomatoo.Main.CategoriesItems.ItemDetailsActivity;
import com.tomatoo.ItemModel;
import com.tomatoo.R;

import java.util.List;

public class AllFragmentAdapter extends RecyclerView.Adapter<AllFragmentAdapter.Holder>{

    Context mContext;
     OnItemInterface mOnItem;
     List<ItemModel> itemList;



    public interface OnItemInterface{
        public  void onItemClick(int position);
    }

    public AllFragmentAdapter(Context mContext,OnItemInterface mOnItem) {
        this.mContext = mContext;
        this.mOnItem=mOnItem;

    }

    @NonNull
    @Override
    public AllFragmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.all_fragment_list_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFragmentAdapter.Holder holder, int i) {
//        ItemModel model=itemList.get(i);
//       TextView name=(TextView) holder.itemView.findViewById(R.id.apple_name_txt);
//       name.setText(model.getName());
//      TextView price=  (TextView) holder.itemView.findViewById(R.id.apple_price_txt);
//      price.setText(model.getPrice());
       mOnItem.onItemClick(i);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class));
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
