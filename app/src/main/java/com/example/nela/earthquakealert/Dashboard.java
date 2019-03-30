package com.example.nela.earthquakealert;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nela.earthquakealert.Adapter.EarthquakeListAdapter;
import com.example.nela.earthquakealert.Model.EventsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static final int PERMISSION_REQUEST_CODE = 1;
    String urlAddress = "http://www.reeas-web.com:3001/earthquakes";
    ListView listView;
    List<EventsData> dataList;
    EventsData d;

    Spinner s1,s2;
    Button b1;
    String state[]=null;
    String S;
    Switch alertSwitch, alertSound, alertVibrate, alertNotification;

    //private BroadcastReceiver broadcastReceiver;
    boolean connected = false;
    SwipeRefreshLayout swipeRefreshLayout;
  //  URL url = new URL("http://www.reeas-web.com:3000/androidclient/route/earthquakes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runtime_permissions();
        checkConnectivity();
        setLayout();
        //TODO: Turn off on settings per user authorization
        //FirebaseMessaging.getInstance().subscribeToTopic("ALERT_NOTIFICATIONS");



   //    HttpURLConnection con = (HttpURLConnection) url.openConnection();
   //     con.setRequestMethod("POST");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                //TODO: Open Dashboard
                Intent i = new Intent(this,Dashboard.class);
            }
        }
    }


    private void showList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject dObj = array.getJSONObject(i);
                                String dt = dObj.getString("datetime");

                                String[] str = dt.split("T");
                                String Date = str[0]; // 004
                                String Time = str[1]; // 034556
                                d = new EventsData(Date, dObj.getString("epicenter"), dObj.getString("hypocenter"), Time, dObj.getString("magnitude"));
                                dataList.add(d);
                            }
                            EarthquakeListAdapter adapter = new EarthquakeListAdapter(dataList,getApplicationContext());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: AdapterHandler Error(Notify to check internet connection)
            }
        }
        ) {
        };
        AdapterHandler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

    private void setLayout() {
        if (connected) {
            setContentView(R.layout.activity_dashboard);
            s1 = findViewById(R.id.spinner2);
            s2 = findViewById(R.id.spinner3);
            b1 = findViewById(R.id.button2);
            listView = findViewById(R.id.dashboard_feed);

            s1.setOnItemSelectedListener(this);
            b1.setOnClickListener(this);


            swipeRefreshLayout = findViewById(R.id.feed_connected);
            dataList = new ArrayList<>();
            swipeFunction();
            showList();

        } else {
            setContentView(R.layout.dashboard_noconnection);
            if(!connected) {
                Toast.makeText(Dashboard.this, "No Internet Connection",Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout = findViewById(R.id.feed_notconnected);
            swipeFunction();
        }
    }
    private void runtime_permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        }
    }
    private void checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else connected = false;
    }
    private void swipeFunction() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnectivity();
                if(!connected) {
                    Toast.makeText(Dashboard.this, "Not Connected",Toast.LENGTH_SHORT).show();
                }
                setLayout();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 6000);
            }
        });
    }
    public static Date parse( String input ) throws java.text.ParseException {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }

        return df.parse( input );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            state=new String[]{"9","8","7","6","5"};
        }
        if(position==1){
            state=new String[]{"5","4","3","2","1"};
        }
        if(position==2){
            state=new String[]{"9","8","7","6","5"};
        }
        ArrayAdapter<String> adt=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,state);
        s2.setAdapter(adt);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View parent) {
        String yearP = s1.getSelectedItem().toString();
        String magn = s2.getSelectedItem().toString();
        S = "http://reeas-web.com:3001/earthquakes/"+ yearP + "?magnitude=" + magn;
    }
}
