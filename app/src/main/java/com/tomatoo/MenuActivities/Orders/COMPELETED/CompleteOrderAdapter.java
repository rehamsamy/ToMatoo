package com.tomatoo.MenuActivities.Orders.COMPELETED;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomatoo.Models.UserOrderModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerItemClickListner;

import java.util.List;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserOrderModel.OrderData> list;
    private RecyclerItemClickListner itemClickListner;

    public CompleteOrderAdapter(Context mcontext, List<UserOrderModel.OrderData> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerItemClickListner listener) {
        itemClickListner = listener;
    }

    @NonNull
    @Override
    public CompleteOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.order_completed_row_item, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteOrderAdapter.ViewHolder viewHolder, int position) {
        viewHolder.orderNum_txtV.setText(mcontext.getString(R.string.order_Num) + "# " + list.get(position).getOrder_id());
        viewHolder.orderProcess_txtV.setText(list.get(position).getOrder_Status());
        viewHolder.time_ago_txtV.setText(list.get(position).getOrder_updated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView completed_icon_img;
        TextView orderNum_txtV, orderProcess_txtV, time_ago_txtV;
        Button return_order_btn;

        public ViewHolder(@NonNull View itemView, final RecyclerItemClickListner listner) {
            super(itemView);
            completed_icon_img = itemView.findViewById(R.id.completed_icon_img);
            orderNum_txtV = itemView.findViewById(R.id.completed_order_number_txtV);
            orderProcess_txtV = itemView.findViewById(R.id.completed_orderState_txtV);
            time_ago_txtV = itemView.findViewById(R.id.completed_order_time_txtV);
            return_order_btn = itemView.findViewById(R.id.completed_return_order_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
