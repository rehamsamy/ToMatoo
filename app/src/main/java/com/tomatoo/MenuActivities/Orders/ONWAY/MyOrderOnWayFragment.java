package com.tomatoo.MenuActivities.Orders.ONWAY;

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

public class MyOrderOnWayFragment extends Fragment {
    List<UserOrderModel.OrderData> wait_list;
    RecyclerView wait_recyclerV;
    WaitingOrdersAdapter waitingOrdersAdapter;
    ProgressBar progressBar;
    TextView no_available_data_txtV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wait_list = getArguments().getParcelableArrayList("wait_list");
            Log.i("wait: ", wait_list.size() + "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myorder_onway_fragment, container, false);
        wait_recyclerV = view.findViewById(R.id.waiting_recyclerV_id);
        no_available_data_txtV = view.findViewById(R.id.no_available_data_txtV_id);
        progressBar = view.findViewById(R.id.waiting_proress_id);

        buildWaitRecyclerV(wait_list);
        return view;
    }

    private void buildWaitRecyclerV(final List<UserOrderModel.OrderData> wait_list) {
        wait_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        wait_recyclerV.setHasFixedSize(true);

        if (wait_list.size() > 0) {
            Log.i("wait: ", wait_list + "5");
            wait_recyclerV.setVisibility(View.VISIBLE);
            no_available_data_txtV.setVisibility(View.GONE);
            waitingOrdersAdapter = new WaitingOrdersAdapter(getActivity(), wait_list);
            wait_recyclerV.setAdapter(waitingOrdersAdapter);

            waitingOrdersAdapter.setOnItemClickListener(new RecyclerItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    // Go to details with Data
                    Intent intent = new Intent(getActivity(), OrderDetails.class);
                    intent.putExtra("order_data", wait_list.get(position));
                    getActivity().startActivity(intent);
                }
            });

        } else {
            wait_recyclerV.setVisibility(View.GONE);
            no_available_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
