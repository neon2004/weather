package com.weather.diegojesuscampos.weather.Util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Diego Jesus Campos on 24/11/2016.
 */
public class VolleyS {
    private static VolleyS mVolleyS = null;
    //Este objeto es la cola que usará la aplicación
    private RequestQueue mRequestQueue;

    private VolleyS(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyS getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyS(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}