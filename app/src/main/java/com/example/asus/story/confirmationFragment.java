package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by asus on 6/28/2017.
 */

public class confirmationFragment extends Fragment {
    LinearLayout rl;
    EditText email,otp;
    TextView upload;
    private DatabaseHandler db;

    public confirmationFragment(){
    }
    private Context context;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
        db = new DatabaseHandler(context);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.confirmation_frag , container, false);
        upload = (TextView) v3.findViewById(R.id.upload);
        rl = (LinearLayout) v3.findViewById(R.id.rl3);
        email = (EditText) v3.findViewById(R.id.email);
        otp = (EditText) v3.findViewById(R.id.otp);
        Typeface monstRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        Typeface monstBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Bold.ttf");
        email.setTypeface(monstRegular);
        otp.setTypeface(monstRegular);
        upload.setTypeface(monstRegular);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title =  ((Main2Activity)getActivity()).maindatasaver.getStoryTitle();
                String desc =  ((Main2Activity)getActivity()).maindatasaver.getStoryDesc();
                byte[] img = ((Main2Activity)getActivity()).maindatasaver.getStoryImg();
                long cid = ((Main2Activity)getActivity()).maindatasaver.get_c_id();
                long sid = db.addstory(new Story(title, img, desc));
                long tid = db.createTransaction(sid,cid);
                Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
                //overridePendingTransition( R.anim.lefttoright, R.anim.stable );

            }
        });
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.confirmation_frag, container, false);
        return v3;
    }
}