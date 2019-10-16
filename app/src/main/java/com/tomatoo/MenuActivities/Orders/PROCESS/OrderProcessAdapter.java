package com.tomatoo.MenuActivities.Orders.PROCESS;

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

public class OrderProcessAdapter extends RecyclerView.Adapter<OrderProcessAdapter.ViewHolder> {

    private Context mcontext;
    private List<UserOrderModel.OrderData> list;
    private RecyclerItemClickListner itemClickListner;

    public OrderProcessAdapter(Context mcontext, List<UserOrderModel.OrderData> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerItemClickListner listener) {
        itemClickListner = listener;
    }


    @NonNull
    @Override
    public OrderProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.order_process_row, viewGroup, false);
        return new ViewHolder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProcessAdapter.ViewHolder viewHolder, int position) {
        viewHolder.orderNum_txtV.setText(mcontext.getString(R.string.order_Num) + "# " + String.valueOf(list.get(position).getOrder_id()));
        viewHolder.orderState_txtV.setText(list.get(position).getOrder_Status());
        viewHolder.order_time_txtV.setText(list.get(position).getOrder_updated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView process_icon_img;
        TextView orderNum_txtV, orderState_txtV, order_time_txtV;
        Button received_order_btn, cancel_order_btn;

        public ViewHolder(@NonNull View itemView, final RecyclerItemClickListner listner) {
            super(itemView);
            process_icon_img = itemView.findViewById(R.id.process_icon_img);
            orderNum_txtV = itemView.findViewById(R.id.order_number_txtV);
            orderState_txtV = itemView.findViewById(R.id.order_state_txtV_id);
            order_time_txtV = itemView.findViewById(R.id.order_time_txtV);
            received_order_btn = itemView.findViewById(R.id.received_order_btn);
            cancel_order_btn = itemView.findViewById(R.id.cancel_order_btn);

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
