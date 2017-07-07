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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.asus.story.R.id.list1;

/**
 * Created by asus on 6/29/2017.
 */

public class CommonFragment extends Fragment {


    public CommonFragment(){

    }
    private Context context;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
    }
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final CommonFragment newInstance(String message)
    {
        CommonFragment f = new CommonFragment();
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

        View v1 =inflater.inflate(R.layout.fragment_common, container, false);

        String message = getArguments().getString(EXTRA_MESSAGE);
        Log.d("MESSAGE","ALLSTORY"+ message);
        TextView TV = (TextView) v1.findViewById(R.id.textView2);
        TV.setText(message);


        return v1;
    }


}
