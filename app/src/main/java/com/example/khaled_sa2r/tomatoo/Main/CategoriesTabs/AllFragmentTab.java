package com.example.khaled_sa2r.tomatoo.Main.CategoriesTabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.khaled_sa2r.tomatoo.Main.CategoriesItems.CategoryItemDetailsActivity;
import com.example.khaled_sa2r.tomatoo.ItemModel;
import com.example.khaled_sa2r.tomatoo.R;

import java.util.ArrayList;

public class AllFragmentTab extends Fragment implements AllFragmentAdapter.OnItemInterface{

    ArrayList<ItemModel> models;
    ItemModel itemModel ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_tab,container,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_all);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//        itemModel=new ItemModel("Apple","Fruit","20$","hellofd dujdsgfdfhdzs xz ghdcvzcc");
//        models.add(itemModel);
//        models.add(itemModel);
//        models.add(itemModel);
//        models.add(itemModel);

        AllFragmentAdapter adapter=new AllFragmentAdapter(getContext(),this);
       recyclerView.setAdapter(adapter);

        return view;


    }

    @Override
    public void onItemClick(int position) {

        Intent intent=new Intent(getContext(), CategoryItemDetailsActivity.class);
        //intent.putExtra("model",itemModel);
        //startActivity(intent);
        Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();

    }
}
