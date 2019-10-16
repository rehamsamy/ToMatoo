package com.tomatoo.Main.CategoriesItems;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tomatoo.R;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.Holder> {
    Context mContext;
    ItemClickListener mItem;

    interface  ItemClickListener {
        void onClick(int position);
    }

    public ShoppingCartAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ShoppingCartAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.shopping_cart_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartAdapter.Holder holder, int i) {
       // mItem.onClick(i);

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class Holder  extends RecyclerView.ViewHolder{
        RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;
        public Holder(@NonNull View itemView) {
            super(itemView);
            viewBackground=itemView.findViewById(R.id.view_background);
            viewForeground=itemView.findViewById(R.id.view_foreground);
        }
    }
}
