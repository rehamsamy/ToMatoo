package com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.PROCESS;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khaled_sa2r.tomatoo.MenuActivities.Orders.CancelOrderActivity;
import com.example.khaled_sa2r.tomatoo.R;

public class MyOrderProcessAdapter extends RecyclerView.Adapter<MyOrderProcessAdapter.Holder> {
    Context mContext;

    public MyOrderProcessAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.process_myorder_fragment_list_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,MyOrderProcessDetailsActivity.class));
                Log.v(MyOrderProcessDetailsActivity.class.getSimpleName(),"suseee");
            }
        });


        MaterialButton cancelOrder=(MaterialButton) holder.itemView.findViewById(R.id.cancel_order_btn);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,CancelOrderActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class Holder  extends RecyclerView.ViewHolder{
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
