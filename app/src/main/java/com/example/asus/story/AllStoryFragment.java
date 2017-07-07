package com.example.asus.story;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.asus.story.R.id.list1;

/**
 * Created by asus on 6/29/2017.
 */

public class AllStoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private DatabaseHandler db;
    private RecyclerView lv;
    private dataAdapter data;
    private Story dataModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mlayoutmanager;

    public AllStoryFragment(){

    }
    private Context context;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
    }
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final AllStoryFragment newInstance(String message)
    {
        AllStoryFragment f = new AllStoryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v1 =inflater.inflate(R.layout.fragment_allstory, container, false);
        lv = (RecyclerView) v1.findViewById(list1);
        String message = getArguments().getString(EXTRA_MESSAGE);
        Log.d("MESSAGE","ALLSTORY"+message);
        //lv.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mlayoutmanager);


        swipeRefreshLayout =(SwipeRefreshLayout) v1.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable(){
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

    private void ShowRecords(){

        db = new DatabaseHandler(context);
        final ArrayList<Story> story = new ArrayList<>(db.getAllStories());

        data=new dataAdapter(context, story);
        lv.setAdapter(data);

        Log.d("DATA",lv.toString());
     //   Toast.makeText(getActivity(), "swipe up", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

}
