package com.weather.diegojesuscampos.weather.Controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weather.diegojesuscampos.weather.Adapter.AdapterInfoWeather;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.Datos.ObjWeather;
import com.weather.diegojesuscampos.weather.Interfaces.IMoveMap;
import com.weather.diegojesuscampos.weather.R;
import com.weather.diegojesuscampos.weather.Util.Constants;
import com.weather.diegojesuscampos.weather.Util.VolleyS;
import android.os.Handler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapWeatherFragment extends BaseVolleyFragment  implements OnMapReadyCallback,IMoveMap {
    private GoogleMap mapa;
    private VolleyS volley;
    private RequestQueue fRequestQueue;
    private String norte;
    private String sur;
    private String este;
    private String oeste;
    private String lat;
    private String lng;
    private String ciudad;
    private AdapterInfoWeather adapter;
    private ListView listView;
    private ArrayList<ObjWeather> items;
    private boolean errorDatos = false;
    private int cont = 0;
    private View view;

    ProgressBar VerticalProgressBar;
    int intValue = 0;
    Handler handler = new Handler();

    public MapWeatherFragment() {
        // Required empty public constructor
    }

    public static MapWeatherFragment newInstance(ObjInfoGeografica objIngoGeo) {
        MapWeatherFragment fragment = new MapWeatherFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TAG_NORTE, objIngoGeo.getNorte());
        args.putString(Constants.TAG_SUR, objIngoGeo.getSur());
        args.putString(Constants.TAG_ESTE, objIngoGeo.getEste());
        args.putString(Constants.TAG_OESTE, objIngoGeo.getOeste());
        args.putString(Constants.LAT, objIngoGeo.getLat());
        args.putString(Constants.LONG, objIngoGeo.getLng());
        args.putString(Constants.CIUDAD, objIngoGeo.getLugar());
        fragment.setArguments(args);
        return fragment;
    }

    public static MapWeatherFragment newInstance() {
        MapWeatherFragment fragment = new MapWeatherFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            norte = getArguments().getString(Constants.TAG_NORTE);
            sur = getArguments().getString(Constants.TAG_SUR);
            este = getArguments().getString(Constants.TAG_ESTE);
            oeste = getArguments().getString(Constants.TAG_OESTE);
            lat = getArguments().getString(Constants.LAT);
            lng = getArguments().getString(Constants.LONG);
            ciudad = getArguments().getString(Constants.CIUDAD);
        }

        // HACEMOS LA LLAMADA AL SERVICIO PARA OBTENER DATOS
        makeRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_weather, container, false);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.rl_map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        listView= (ListView) view.findViewById(R.id.listWeather);

        items = new ArrayList<ObjWeather> ();
        adapter = new AdapterInfoWeather(getActivity().getApplicationContext(),R.layout.item_list_wether,items);
        adapter.callback = MapWeatherFragment.this;
        listView.setAdapter(adapter);

        VerticalProgressBar = (ProgressBar)view.findViewById(R.id.barraTemp);

        return view;
    }

    @Override
    public void moveMap(ObjWeather infoWeather) {
        updateUI(infoWeather);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setMapToolbarEnabled(false);
        updateUI();
    }

    private void updateUI(ObjWeather infoWeather) {
        ActualizarMapa(infoWeather);


    }

    private void updateUI() {
        ActualizarMapa(null);

    }

    private void ActualizarMapa(final ObjWeather infoWeather){
        VerticalProgressBar.setProgress(0);
        intValue = 0;
        double latitud = 0;
        double longuitud = 0;
        if(infoWeather != null) {
             latitud = Double.parseDouble(infoWeather.getLat());
             longuitud = Double.parseDouble(infoWeather.getLng());

            mapa.addMarker(new MarkerOptions()
                    .position(new LatLng(latitud, longuitud))
                    .title(infoWeather.getStationName())
                    .snippet(getString(R.string.temp)+infoWeather.getTemperature()+"º"));
        }else{
             latitud = Double.parseDouble(lat);
             longuitud = Double.parseDouble(lng);
        }

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longuitud), 12);
        mapa.moveCamera(camUpd1);

        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                .title(ciudad)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        new Thread(new Runnable() {

            @Override
            public void run() {

               int vTemp = infoWeather != null ? Integer.parseInt(infoWeather.getTemperature()) : 0;

                while(intValue < vTemp)
                {
                    intValue++;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            VerticalProgressBar.setProgress(intValue);

                        }
                    });try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            }
        }).start();



    }

    @Override
    public void onStart() {
        super.onStart();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void makeRequest(){
        String url = Constants.URL_WEATHER;
        url = url.replace(Constants.URLPLACE,ciudad);
        url = url.replace(Constants.URLNORTE,norte);
        url = url.replace(Constants.URLSUR,sur);
        url = url.replace(Constants.URLESTE,este);
        url = url.replace(Constants.URLOESTE,oeste);
        url = url.replace(Constants.URLUSERNAME,Constants.USERNAME1);
        if(!errorDatos || cont <= Constants.TAG_NUM_INTENTOS) {
            if (cont % 2 == 0 ) {
                url = url.replace(Constants.URLUSERNAME, Constants.USERNAME1);
            } else {
                url = url.replace(Constants.URLUSERNAME, Constants.USERNAME2);
            }
        }else{
            Snackbar snackbar = Snackbar.make(view, getString(R.string.errorDatos), Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                 items = parseJson(jsonObject);
                adapter.clear();
                adapter.updateObjInfoWeatherList(items);
                onConnectionFinished();

                if(!errorDatos){
                    cont = 0;
                    if(items.size() == 0){
                        Snackbar snackbar = Snackbar.make(view, getString(R.string.noDatos), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }else{
                    makeRequest();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(
                        volleyError.toString()
                );
            }
        });
        addToQueue(request);
    }


    public ArrayList<ObjWeather> parseJson(JSONObject jsonObject){
        // Variables locales
        ArrayList<ObjWeather> arrayObjWeather = new ArrayList();
        JSONArray jsonArray= null;
        errorDatos = false;
        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray(Constants.TAG_WEATHEROBSERVATION);

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);


                    ObjWeather obj = new ObjWeather(objeto.getString(Constants.TAG_LONGUITUD),
                            objeto.getString(Constants.TAG_LATITUD),
                            objeto.getString(Constants.TAG_TEMPERATURA), objeto.getString(Constants.TAG_HUMEDAD),
                            objeto.getString(Constants.TAG_ESTACION), objeto.getString(Constants.TAG_CONDICIONCLIMA));

                    arrayObjWeather.add(obj);

                } catch (JSONException e) {
                    errorDatos = true;
                    cont++;
                    Log.e("ERROR PARSEO", "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            errorDatos = true;
            cont++;
        }

        return arrayObjWeather;
    }
}
