package com.example.asus.story;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.example.asus.story.R.id.list1;

/**
 * Created by asus on 6/29/2017.
 */

public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private DatabaseHandler db;
    private RecyclerView lv;
    private dataAdapter data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mlayoutmanager;

    public PopularFragment(){

    }
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_popular, container, false);
        lv = (RecyclerView) v1.findViewById(list1);
        //lv.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mlayoutmanager);


        swipeRefreshLayout = (SwipeRefreshLayout) v1.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        // call function
                                        ShowRecords();
                                    }
                                }
        );
        return v1;
    }

    public void onRefresh() {
        ShowRecords();
    }

    private void ShowRecords() {
        db = new DatabaseHandler(context);
        final ArrayList<Story> story = new ArrayList<>(db.getAllStoryByCategory("Popular"));

        data = new dataAdapter(context, story);

        lv.setAdapter(data);
        Log.d("DATA", lv.toString());
        swipeRefreshLayout.setRefreshing(false);

    }
}