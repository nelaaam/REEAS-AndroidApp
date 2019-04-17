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
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nela.earthquakealert.Adapter.EarthquakeEventsAdapter;
import com.example.nela.earthquakealert.Handler.AdapterHandler;
import com.example.nela.earthquakealert.Model.EventsData;
import com.example.nela.earthquakealert.Service.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Dashboard";
    public static final int PERMISSION_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;
    String baseUrl = "http://www.reeas-web.com:3001/earthquakes";
    String requestUrl;
    ArrayList<EventsData> dataList = new ArrayList<>();
    EventsData d;
    Spinner spinnerYear, spinnerMagnitude;
    String yearSelected, magSelected;
    Button filterButton;
    int start = 0;
    int visibleItemCount, totalItemCount, pastVisibleItems, previousTotal = 0, viewThreshold = 10;
    boolean connected = false, yearFilterAdded = false, magFilterAdded = false, isLoading = true;
    Button b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runtime_permissions();
        checkConnectivity();
        setLayout();
    }

    private void setLayout() {
        if (connected) {
            if (!yearFilterAdded && !magFilterAdded) {
                requestUrl = baseUrl;
            }
            Log.d(TAG, "Connected");
            setContentView(R.layout.activity_dashboard);
            recyclerView = findViewById(R.id.dashboard_feed);
            recyclerView.setHasFixedSize(true);
            rvLayoutManager = new LinearLayoutManager(this);
            getEarthquakeEvents();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = rvLayoutManager.getChildCount();
                    totalItemCount = rvLayoutManager.getItemCount();
                    Log.d(TAG, "VIC = " + visibleItemCount);
                    Log.d(TAG, "TIC = " + totalItemCount);
                    if (dy > 0) {
                        checkConnectivity();
                        if(connected){
                            if (isLoading) {
                                if (totalItemCount > previousTotal) {
                                    isLoading = false;
                                    previousTotal = totalItemCount;
                                }
                            } else if (!isLoading) {
                                start = start + 10;
                                getEarthquakeEvents();
                                isLoading = true;
                            }
                        }else {
                            Toast.makeText(Dashboard.this, "Check your internet connection", Toast.LENGTH_LONG).show();
                        }

                    }


                }
            });

            spinnerYear = findViewById(R.id.spinner2);
            spinnerMagnitude = findViewById(R.id.spinner3);
            filterButton = findViewById(R.id.button2);

            filterButton.setEnabled(false);

            Intent i = new Intent(getApplicationContext(), GPSTracker.class);
            startService(i);

            spinnerYear.setOnItemSelectedListener(this);
            spinnerMagnitude.setOnItemSelectedListener(this);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start = 0;
                    getEarthquakeEvents();
                }
            });

        } else if (!connected) {
            Toast.makeText(Dashboard.this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else connected = false;
    }

    private void getEarthquakeEvents() {
        if (yearFilterAdded && !magFilterAdded)
            requestUrl = baseUrl + "/" + yearSelected + "?start=" + start;
        else if (magFilterAdded && !yearFilterAdded)
            requestUrl = baseUrl + "?start=" + start + "&&magnitude=" + magSelected;
        else if (magFilterAdded && yearFilterAdded)
            requestUrl = baseUrl + "/" + yearSelected + "/" + magSelected + "?start=" + start;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            if (status.equals("ok")) {
                                JSONArray array = obj.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject dObj = array.getJSONObject(i);
                                    String dt = dObj.getString("datetime");
                                    String[] str = dt.split("T");
                                    String date = str[0]; // 004
                                    String time = str[1]; // 034556
                                    String[] str2 = time.split("Z");
                                    time = str2[0];
                                    String latitude = String.format("%.2f", Double.valueOf(dObj.getString("latitude")));
                                    String longitude = String.format("%.2f", Double.valueOf(dObj.getString("longitude")));
                                    String location = dObj.getString("location");
                                    String magnitude = String.format("%.2f", Double.valueOf(dObj.getString("magnitude")));
                                    d = new EventsData(date, time, latitude, longitude, location, magnitude);
                                    dataList.add(d);
                                }
                                rvAdapter = new EarthquakeEventsAdapter(dataList);
                                recyclerView.setLayoutManager(rvLayoutManager);
                                recyclerView.setAdapter(rvAdapter);
                            } else if (status.equals("end")) {
                                Toast.makeText(Dashboard.this, "No More Available Events", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }
        ) {
        };
        AdapterHandler.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

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

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent i = new Intent(this, Dashboard.class);
            }
        }
    }

    private void runtime_permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        yearSelected = spinnerYear.getSelectedItem().toString();
        magSelected = spinnerMagnitude.getSelectedItem().toString();
        if (!filterButton.isEnabled()) {
            if (!yearSelected.equals("SELECTED")) yearFilterAdded = true;
            if (!magSelected.equals("SELECTED")) magFilterAdded = true;
            filterButton.setEnabled(true);
        } else if (filterButton.isEnabled()) {
            if (yearSelected.equals("SELECTED")) yearFilterAdded = false;
            if (magSelected.equals("SELECTED")) magFilterAdded = false;
            if (magSelected.equals("SELECTED") && yearSelected.equals("SELECTED")) {
                filterButton.setEnabled(false);
                requestUrl = baseUrl;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        yearSelected = spinnerYear.getSelectedItem().toString();
        magSelected = spinnerMagnitude.getSelectedItem().toString();
        if (filterButton.isEnabled()) {
            if (yearSelected.equals("SELECTED")) yearFilterAdded = false;
            if (magSelected.equals("SELECTED")) magFilterAdded = false;
            if (magSelected.equals("SELECTED") && yearSelected.equals("SELECTED")) {
                filterButton.setEnabled(false);
                requestUrl = baseUrl;
            }
        }
    }
}
