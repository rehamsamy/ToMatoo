package com.tomatoo.MenuActivities.Orders.PROCESS;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tomatoo.MenuActivities.Orders.OrderDetails;
import com.tomatoo.Models.UserOrderModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerItemClickListner;

import java.util.List;

public class MyOrderProcessFragment extends Fragment {

    List<UserOrderModel.OrderData> process_list;
    RecyclerView process_recyclerV;
    OrderProcessAdapter ordersAdapter;
    ProgressBar progressBar;
    TextView no_data_txtV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            process_list = getArguments().getParcelableArrayList("process_list");
            Log.i("process: ", process_list + "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myorder_process_fragment, container, false);
        process_recyclerV = view.findViewById(R.id.process_recyclerV_id);
        progressBar = view.findViewById(R.id.process_prgressBar_id);
        no_data_txtV = view.findViewById(R.id.process_no_available_data_txtV_id);

        buildProcessRecyclerV(process_list);
        return view;
    }

    private void buildProcessRecyclerV(final List<UserOrderModel.OrderData> list) {
        process_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        process_recyclerV.setHasFixedSize(true);
        if (process_list.size() > 0) {
            process_recyclerV.setVisibility(View.VISIBLE);
            no_data_txtV.setVisibility(View.GONE);
            ordersAdapter = new OrderProcessAdapter(getActivity(), list);
            process_recyclerV.setAdapter(ordersAdapter);

            ordersAdapter.setOnItemClickListener(new RecyclerItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    // Go to details with Data
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    intent.putExtra("order_data", list.get(position));
                    getActivity().startActivity(intent);
                }
            });

        } else {
            process_recyclerV.setVisibility(View.GONE);
            no_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}