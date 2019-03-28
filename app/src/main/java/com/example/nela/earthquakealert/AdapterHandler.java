package com.example.nela.earthquakealert;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AdapterHandler {
    private static AdapterHandler mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    public AdapterHandler(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized AdapterHandler getInstance(Context context){
        if (mInstance == null) {
            mInstance = new AdapterHandler(context);
        }

        return mInstance;
    }

    public <T>void addToRequestQue(Request<T> request) {
        requestQueue.add(request);
    }
}
