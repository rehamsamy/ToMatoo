package com.example.khaled_sa2r.tomatoo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCompareListActivity extends AppCompatActivity {
@BindView(R.id.compare_by_recycler)
    RecyclerView recyclerView;
CompareByListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_compare_list2);
        ButterKnife.bind(this);
        adapter=new CompareByListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
