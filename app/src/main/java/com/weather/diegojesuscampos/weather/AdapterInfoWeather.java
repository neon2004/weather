package com.weather.diegojesuscampos.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterInfoWeather extends ArrayAdapter<ObjWeather> {
    private Context ctx;
    private ArrayList<ObjWeather> items;

    public AdapterInfoWeather(Context context, int resource, ArrayList<ObjWeather> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.ctx = context;
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
            convertView = inflater.inflate(R.layout.item_list_busqueda, parent, false);
            viewHolder.estacion = (TextView) convertView.findViewById(R.id.txtCiudad);
            viewHolder.condicion = (TextView) convertView.findViewById(R.id.txtPais);
            viewHolder.temperatura = (TextView) convertView.findViewById(R.id.txtCiudad);
            viewHolder.humedad = (TextView) convertView.findViewById(R.id.txtPais);
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
