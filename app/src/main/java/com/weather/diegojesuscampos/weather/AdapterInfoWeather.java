package com.weather.diegojesuscampos.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterInfoWeather extends ArrayAdapter<ObjInfoGeografica> {
    private Context ctx;
    private ArrayList<ObjInfoGeografica> items;
    public IShowWeather callback;

    public AdapterInfoWeather(Context context, int resource, ArrayList<ObjInfoGeografica> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.ctx = context;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ObjInfoGeografica getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ObjInfoGeografica objInfoGeo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list_busqueda, parent, false);
            viewHolder.ciudad = (TextView) convertView.findViewById(R.id.txtCiudad);
            viewHolder.pais = (TextView) convertView.findViewById(R.id.txtPais);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ciudad.setText(objInfoGeo.getLugar()+", "+objInfoGeo.getCiudad());
        viewHolder.pais.setText(objInfoGeo.getPais());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.showWeatherPLaces(getItem(position));
            }
        });

        return convertView;
    }

    static class ViewHolder
    {
        TextView ciudad;
        TextView pais;

    }
}
