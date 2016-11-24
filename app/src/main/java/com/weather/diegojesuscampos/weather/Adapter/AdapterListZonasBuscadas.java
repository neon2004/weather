package com.weather.diegojesuscampos.weather.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.Datos.ZonasViewHolder;
import com.weather.diegojesuscampos.weather.Interfaces.IShowWeather;
import com.weather.diegojesuscampos.weather.R;

import java.util.ArrayList;


public class AdapterListZonasBuscadas extends FirebaseRecyclerAdapter<ObjInfoGeografica, ZonasViewHolder> {

    Activity parentActivity;
    ZonasViewHolder.MyViewHolderClickListener myViewHolderClickListener;

    private String TAG = getClass().getSimpleName();

    public AdapterListZonasBuscadas(Activity mActivity, Class<ObjInfoGeografica> modelClass, int modelLayout, Class<ZonasViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        parentActivity = mActivity;
    }


    @Override
    public ZonasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZonasViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        // adding my MyViewHolderClickListener here
        viewHolder.setCustomOnClickListener(myViewHolderClickListener);
        return viewHolder;
    }

    @Override
    protected void populateViewHolder(ZonasViewHolder viewHolder, final ObjInfoGeografica model, final int position) {

        Log.d(TAG, model.toString());
        Log.d(TAG, getRef(position).getKey());

        // bind view
        viewHolder.bindToStudent(model);
    }


    public void setMyViewHolderClickListener(ZonasViewHolder.MyViewHolderClickListener listener){
        this.myViewHolderClickListener = listener;
    }
}