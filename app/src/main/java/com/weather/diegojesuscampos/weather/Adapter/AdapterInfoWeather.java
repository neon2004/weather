package com.weather.diegojesuscampos.weather.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.Interfaces.IMoveMap;
import com.weather.diegojesuscampos.weather.Datos.ObjWeather;
import com.weather.diegojesuscampos.weather.R;

import java.util.ArrayList;


public class AdapterInfoWeather extends ArrayAdapter<ObjWeather> {
    private Context ctx;
    private ArrayList<ObjWeather> items;
    public IMoveMap callback;
    private View viewAnt = null;

    public AdapterInfoWeather(Context context, int resource, ArrayList<ObjWeather> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.ctx = context;
    }

    public void updateObjInfoWeatherList(ArrayList<ObjWeather> newlist) {
        items.clear();
        items.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ObjWeather getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ObjWeather objWeather = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_wether, parent, false);
            viewHolder.estacion = (TextView) convertView.findViewById(R.id.txtEstacion);
            viewHolder.condicion = (TextView) convertView.findViewById(R.id.txtValorCondicion);
            viewHolder.temperatura = (TextView) convertView.findViewById(R.id.txtValorTemp);
            viewHolder.humedad = (TextView) convertView.findViewById(R.id.txtValorHumedad);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.estacion.setText(objWeather.getStationName());
        viewHolder.condicion.setText(objWeather.getWeatherCondition());
        viewHolder.temperatura.setText(objWeather.getTemperature());
        viewHolder.humedad.setText(objWeather.getHumidity());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.moveMap(getItem(position));
            }
        });

        return convertView;
    }



    static class ViewHolder
    {
        TextView estacion;
        TextView condicion;
        TextView temperatura;
        TextView humedad;
    }
}
