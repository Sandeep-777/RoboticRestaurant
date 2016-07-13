package com.example.sandeep.parsingtest;

/**
 * Created by sandeep on 6/30/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodActivity extends ActionBarActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    ImageView image;
    String title = "Menu";
    static String hostname;
    String tablenumber;
    int image_id = R.drawable.menu1;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initControls() {

        sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        hostname = sharedpreferences.getString("IP","192.168.64.1");
        tablenumber = sharedpreferences.getString("Table","23");
        //Toast.makeText(this,hostname+"  "+tablenumber,Toast.LENGTH_LONG).show();

        image = (ImageView) findViewById(R.id.image_cusine);
        image.setImageResource(image_id);
        //toolbar
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_cusine);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setPalette();

        toolbar = (Toolbar) findViewById(R.id.toolbar_cusine);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("MENU");
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.food_activity_layout);


        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_cusine);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_main_header, null);
        navigationView.addHeaderView(headerView);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

//                menuItem.setChecked(true);
//                mDrawerLayout.closeDrawers();
//                Toast.makeText(FoodActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        DesignDemoPagerAdapter adapter = new DesignDemoPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_foods);
        viewPager.setAdapter(adapter);

    }

    private void setPalette() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.primary_dark);
                int primary = getResources().getColor(R.color.primary);
                collapsingToolbarLayout.setContentScrimColor(primaryDark);
                collapsingToolbarLayout.setStatusBarScrimColor(primaryDark);
            }
        });

    }

    public static class DesignDemoFragment extends Fragment {
        private RecyclerView recyclerView;

        public DesignDemoFragment() {

        }

        public static DesignDemoFragment newInstance(int tabPosition) {
            DesignDemoFragment fragment = new DesignDemoFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View view = inflater.inflate(R.layout.drawer_main_header, container, false);

            view = inflater.inflate(R.layout.food_fragment_list, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
            CardView cardView;
            ArrayList<Items> item;
            RecyclerView.Adapter mAdapter;

            final int[] icons = {R.drawable.dalbhat, R.drawable.dhindo, R.drawable.khichuri, R.drawable.kwati, R.drawable.phapar, R.drawable.sandheko_gundruk, R.drawable.selroti, R.drawable.yomari};
            final String[] versions = {"DalBhat", "Dhindo", "Khichuri", "Kwati", "Phapar", "Gundruk", "SellRoti", "Yomari"};
            final String[] cost = {"Rs 100", "Rs 200", "Rs 120", "Rs 50", "Rs 85", "Rs 45", "Rs 25", "Rs 40"};
            final int[] number = {0, 0, 0, 0, 0, 0, 0, 0};

            item = new ArrayList<Items>();

            for (int i = 0; i < versions.length; i++) {
                Items feed = new Items();
                feed.setTitle(versions[i]);
                feed.setThumbnail(icons[i]);
                feed.setNumber(number[i]);
                feed.setCost(cost[i]);
                item.add(feed);
            }

            recyclerView.setHasFixedSize(true);
            // GridView
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            // create an Object for Adapter
            mAdapter = new FoodItemAdapter(item);
            // set the adapter object to the RecyclerView
            recyclerView.setAdapter(mAdapter);
            return view;
        }
    }


    static class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {

        public DesignDemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DesignDemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence tab_title;
            switch (position) {
                case 0:
                    tab_title = "Cusines";
                    break;
                default:
                    tab_title = "food";
            }
            return tab_title;
        }
    }

    public void onDoneButtonClicked(View v) {
        Intent intent = new Intent(v.getContext(), FoodActivity.class);
        v.getContext().startActivity(intent);
        SendData send = new SendData();
        String write_data = "table_number" + ":" + tablenumber + ";";
        try {

            send.commitToFile(write_data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MyClientTask myClientTask = new MyClientTask(hostname);
        myClientTask.execute();
        Toast.makeText(v.getContext(), "Your order has been placed.", Toast.LENGTH_LONG).show();
    }
    public void onIpButtonClicked(View v) {
        EditText et_ip = (EditText) findViewById(R.id.et_ipname);
        Button bt_ip = (Button) findViewById(R.id.bt_ipname);

        if(et_ip.getVisibility() == View.INVISIBLE){
            et_ip.setVisibility(View.VISIBLE);
            bt_ip.setText("SET IP");
            bt_ip.setBackgroundColor(getResources().getColor(R.color.like));
        }
        else if(et_ip.getVisibility() == View.VISIBLE){
            hostname = et_ip.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("IP", et_ip.getText().toString());
            editor.commit();

            bt_ip.setText("CHANGE IP");
            et_ip.setVisibility(View.INVISIBLE);
            bt_ip.setBackgroundColor(getResources().getColor(R.color.primary));
            Toast.makeText(v.getContext(), "Your ip settings has been changed to " + hostname, Toast.LENGTH_LONG).show();
        }
    }

    public void onTableButtonClicked(View v) {
        EditText et_tn = (EditText) findViewById(R.id.et_table_number);
        Button bt_tn = (Button) findViewById(R.id.bt_table_number);
        if(et_tn.getVisibility() == View.INVISIBLE){
            et_tn.setVisibility(View.VISIBLE);
            bt_tn.setText("SET");
            bt_tn.setBackgroundColor(getResources().getColor(R.color.like));
        }
        else if(et_tn.getVisibility() == View.VISIBLE){
            tablenumber = et_tn.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Table", et_tn.getText().toString());
            editor.commit();
            bt_tn.setText("TABLE NUMBER");
            et_tn.setVisibility(View.INVISIBLE);
            bt_tn.setBackgroundColor(getResources().getColor(R.color.primary));
            Toast.makeText(v.getContext(), "Your table number has been changed to " + tablenumber, Toast.LENGTH_LONG).show();
        }
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask() {
            dstAddress = "192.168.64.1";
            dstPort = 8080;
            msgToServer = "Mandy is super gay";
        }
        MyClientTask(String host) {
            dstAddress = host;
            dstPort = 8080;
            msgToServer = "Mandy is super gay";
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "data.txt");
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    byte[] valuesAscii = line.getBytes("US-ASCII");
                    dataOutputStream.write(valuesAscii);
                }
                fileReader.close();
                dataOutputStream.close();
                SendData send = new SendData();             //
                send.deletefile();                          //
                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            super.onPostExecute(result);
        }


    }

}