package com.tomatoo.MenuActivities.Orders.COMPELETED;

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
import com.tomatoo.MenuActivities.Orders.PROCESS.OrderProcessAdapter;
import com.tomatoo.Models.UserOrderModel;
import com.tomatoo.R;
import com.tomatoo.interfaces.RecyclerItemClickListner;

import java.util.List;

public class MyOrderCompletedFragment extends Fragment {

    List<UserOrderModel.OrderData> complete_list;
    RecyclerView complete_recyclerV;
    CompleteOrderAdapter completeOrderAdapter;
    ProgressBar progressBar;
    TextView no_available_data_txtV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            complete_list = getArguments().getParcelableArrayList("complete_list");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myorder_completed_fragment, container, false);
        complete_recyclerV = view.findViewById(R.id.completed_recyclerV_id);
        progressBar = view.findViewById(R.id.completed_prgressBar_id);
        no_available_data_txtV = view.findViewById(R.id.completed_no_available_data_txtV_id);

        buildCompleteRecyclerV(complete_list);
        return view;
    }

    private void buildCompleteRecyclerV(final List<UserOrderModel.OrderData> complete_list) {
        complete_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        complete_recyclerV.setHasFixedSize(true);

        if (complete_list.size() > 0) {
            complete_recyclerV.setVisibility(View.VISIBLE);
            no_available_data_txtV.setVisibility(View.GONE);

            completeOrderAdapter = new CompleteOrderAdapter(getActivity(), complete_list);
            complete_recyclerV.setAdapter(completeOrderAdapter);

            completeOrderAdapter.setOnItemClickListener(new RecyclerItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    // Go to details with Data
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    intent.putExtra("order_data", complete_list.get(position));
                    getActivity().startActivity(intent);
                }
            });
        } else {
            complete_recyclerV.setVisibility(View.GONE);
            no_available_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
