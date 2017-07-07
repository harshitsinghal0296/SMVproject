package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView maintext,uploadtext;
    private DrawerLayout drawer;
    private View underlineview1,underlineview2;
    Intent intent;
    private DatabaseHandler db;
    ViewPager viewPager,mViewPager;
    TabLayout tabLayout;
    Typeface monstRegular,monstBold;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;


    // URL to get category JSON
    private static String url = "http://kookyapps.com/smv/api/newsType";
    ArrayList<HashMap<String,String>> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CATEGORY AS ARRAYLIST
        categoryList = new ArrayList<>();
        new GetCategory().execute();

        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        // CALL DRAWER
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Typeface monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        maintext = (TextView) findViewById(R.id.textView5);
        uploadtext = (TextView) findViewById(R.id.likevw);
        maintext.setTypeface(monstRegular);
        uploadtext.setTypeface(monstRegular);
        underlineview1 = (View) findViewById(R.id.underlineView);
        underlineview2 = (View) findViewById(R.id.underlineView1);
        uploadtext.setTextColor(getResources().getColor(R.color.tabcolor));

        db = new DatabaseHandler(getApplicationContext());
        int count = db.CountCategory();
        if (count == 0)
        {
            //Log.d("ADD","ADD");
            long id1 = db.createCategory(new Categories(1,"Popular"));
            long id2 = db.createCategory(new Categories(2,"Featured"));
            long id3 = db.createCategory(new Categories(3,"My Favourite"));
            //Log.d("id1 "+id1,"id2 "+id2);
        }


    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(monstBold);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = getFragments();
        List<String> Title = getTitleArray();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments, Title);

        viewPager.setAdapter(adapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        for (int i=0;i<categoryList.size();i++)
        {
            fList.add(CommonFragment.newInstance(categoryList.get(i).get("id")));
        }

        return fList;
    }
    private List<String> getTitleArray() {
        List<String> fList = new ArrayList<String>();
        for (int i=0;i<categoryList.size();i++)
        {
            fList.add(categoryList.get(i).get("name"));
        }

        return fList;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> mFragmentTitleList;

        public ViewPagerAdapter(FragmentManager manager,List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
            super(manager);
            this.mFragmentList = mFragmentList;
            this.mFragmentTitleList = mFragmentTitleList;

        }

        @Override
        public Fragment getItem(int position) {return mFragmentList.get(position);}

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        maintext.setTextColor(getResources().getColor(R.color.tabactivecolor));
        underlineview2.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("c_id");
                        String name = c.getString("c_name");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);


                        // adding contact to contact list
                        categoryList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            viewPager = (ViewPager) findViewById(R.id.viewpagermain);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            changeTabsFont();
            uploadtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("CATEGORY",categoryList);
                    startActivity(intent);
                    //overridePendingTransition( R.anim.righttoleft, R.anim.stable );
                    finish();


                }
            });

            for (int i=0;i<categoryList.size();i++)
            {
                Log.d("CAT","DATA"+categoryList.get(i));
            }
        }

    }
}



