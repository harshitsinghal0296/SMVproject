package com.example.asus.story;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class confirmationFragment extends Fragment {
    public static final String REGISTER_URL = "http://kookyapps.com/smv/api/uploadNews/";
    public static final String OTP_URL = "http://kookyapps.com/smv/api/verifyOtp/";

    public static final String KEY_TITLE = "n_title";
    public static final String KEY_DESC = "n_desc";
    public static final String KEY_CAT = "n_cat";
    public static final String KEY_IMG = "n_img";
    public static final String KEY_EMAIL = "n_email";
    int responseFlag = 0,OtpresponseFlag = 0;
    int inside = 1;
    LinearLayout rl;
    public EditText email,otp;
    TextView upload;
    private DatabaseHandler db;
    boolean FTPstatus = false,onPost = false;
    private MyFTPClientFunctions mFtpClient = null;
    private ProgressDialog pd;
    private String UploadedUrl;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (msg.what == 0) {
            }
            else if (msg.what == 1) {

            } else if (msg.what == 2) {
                //Toast.makeText(getContext(), "Uploaded Successfully!", Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(getContext(), "Disconnected Successfully!",
                        Toast.LENGTH_LONG).show();
            }  else if (msg.what == 4) {
                Toast.makeText(getContext(), "Failed Uploading!",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "Unable to Perform Action!",
                        Toast.LENGTH_LONG).show();
            }

        }

    };

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
        email.setTypeface(monstRegular);
        email.getText().equals("");
        otp.setTypeface(monstRegular);
        upload.setTypeface(monstRegular);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB OPERATION
              /*  String title =  ((Main2Activity)getActivity()).maindatasaver.getStoryTitle();
                String desc =  ((Main2Activity)getActivity()).maindatasaver.getStoryDesc();
                byte[] img = ((Main2Activity)getActivity()).maindatasaver.getStoryImg();
                long cid = ((Main2Activity)getActivity()).maindatasaver.get_c_id();
                long sid = db.addstory(new Story(title, img, desc));
                long tid = db.createTransaction(sid,cid);
               */
              if(inside == 1) {
                  if (email.getText().toString().trim().equals("")) {
                      email.setError("Email is Required");
                  } else {
                      String em = email.getText().toString();
                      ((Main2Activity) getActivity()).storysaver.setEmail(em);

                      if (isOnline(getContext())) {
                          new MyAwesomeAsyncTask().execute();

                      } else {
                          Toast.makeText(getActivity(),
                                  "Please check your internet connection!",
                                  Toast.LENGTH_LONG).show();
                      }
                  }
              }
              else
              {

                  int OTPCHK = verifyOtp();

                  if(OTPCHK == 1)
                  {
                      Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                      getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                      getActivity().finish();
                      //overridePendingTransition( R.anim.lefttoright, R.anim.stable );
                  }
                  else
                  {
                      Toast.makeText(getActivity(), "OTP Verification Failed ! Try Again", Toast.LENGTH_SHORT).show();
                  }
              }
            }
        });
        Log.d("TAG", "FILE PATH"+ ((Main2Activity) getActivity()).storysaver.getPath());
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.confirmation_frag, container, false);
        return v3;
    }


    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class MyAwesomeAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pd;
        MyFTPClientFunctions mFtpClient = new MyFTPClientFunctions();


        @Override
        protected void onPreExecute() {
            //Create progress dialog here and show it
            pd = ProgressDialog.show(getContext(), "", "Uploading...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            FTPstatus = mFtpClient.ftpConnect("166.62.28.118","smv@kookyapps.com","kooky_smv",21);
            if (FTPstatus) {
                boolean uploaded = false;
                //Log.d("TAG", "Connection Success");
                String getCurrentDir = mFtpClient.ftpGetCurrentWorkingDirectory();
                //Log.d("TAG", "CURRENT DIR"+getCurrentDir);
                //Log.d("TAG", "FILE PATH"+imagefilePath);
                String newname = System.currentTimeMillis() + ".jpg";
                UploadedUrl = "http://kookyapps.com/smv/uploads/"+newname;
                // Log.d("URL","URL - "+url);

                String imagefilePath = ((Main2Activity) getActivity()).storysaver.getPath();
                uploaded = mFtpClient.ftpUploadImage(imagefilePath,newname,"/",getActivity());
                if(uploaded) {
                    Log.d("UPLOAD","YES"); handler.sendEmptyMessage(2);
                    ((Main2Activity) getActivity()).storysaver.setUrl(UploadedUrl);
                }
                else{Log.d("UPLOAD","NO"); handler.sendEmptyMessage(4);}

                mFtpClient.ftpDisconnect();

            }else {
                handler.sendEmptyMessage(-1);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pd.dismiss();
            int res = send();
            if(res == 1)
            {
                inside = 2;
                Toast.makeText(getContext(), "Check Email for OTP !", Toast.LENGTH_LONG).show();
            }
            //update your listView adapter here
            //Dismiss your dialog

        }

    }

    private int send(){

        String title = ((Main2Activity) getActivity()).storysaver.getCaption();
        String desc = ((Main2Activity) getActivity()).storysaver.getDesc();
        String cat = ((Main2Activity) getActivity()).storysaver.getCat_id();
        String img = ((Main2Activity) getActivity()).storysaver.getUrl();
        String email = ((Main2Activity) getActivity()).storysaver.getEmail();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(KEY_TITLE,title);
        params.put(KEY_DESC,desc);
        params.put(KEY_CAT,cat);
        params.put(KEY_IMG,img);
        params.put(KEY_EMAIL,email);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,REGISTER_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString(4));
                            responseFlag = response.getInt("response");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
        return responseFlag;
    }

    private int verifyOtp(){

        if(otp.getText().toString().trim().equals(""))
        {
            Toast.makeText(getContext(), "Otp Please!", Toast.LENGTH_LONG).show();
            otp.setError("Otp Required");
        }
        else {
            String email = ((Main2Activity) getActivity()).storysaver.getEmail();
            String otpParam = otp.getText().toString();


            HashMap<String, String> params = new HashMap<String, String>();
            params.put("n_email", email);
            params.put("n_otp", otpParam);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, OTP_URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                Log.d("Response:%n %s", response.toString(4));
                                OtpresponseFlag = response.getInt("response");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(req);
        }
        return OtpresponseFlag;
    }

}