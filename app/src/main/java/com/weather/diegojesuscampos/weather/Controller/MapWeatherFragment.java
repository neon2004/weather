package com.weather.diegojesuscampos.weather.Controller;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.weather.diegojesuscampos.weather.Interfaces.IMoveMap;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.Datos.ObjWeather;
import com.weather.diegojesuscampos.weather.R;
import com.weather.diegojesuscampos.weather.Util.Constants;
import com.weather.diegojesuscampos.weather.Util.VolleyS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapWeatherFragment extends BaseVolleyFragment  implements OnMapReadyCallback,IMoveMap {
    private GoogleMap mapa;
    private OnFragmentInteractionListener mListener;
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

    public MapWeatherFragment() {
        // Required empty public constructor
    }

    public static MapWeatherFragment newInstance(ObjInfoGeografica objIngoGeo) {
        MapWeatherFragment fragment = new MapWeatherFragment();
        Bundle args = new Bundle();
        args.putString(Constants.NORTE, objIngoGeo.getNorte());
        args.putString(Constants.SUR, objIngoGeo.getSur());
        args.putString(Constants.ESTE, objIngoGeo.getEste());
        args.putString(Constants.OESTE, objIngoGeo.getOeste());
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
            norte = getArguments().getString(Constants.NORTE);
            sur = getArguments().getString(Constants.SUR);
            este = getArguments().getString(Constants.ESTE);
            oeste = getArguments().getString(Constants.OESTE);
            lat = getArguments().getString(Constants.LAT);
            lng = getArguments().getString(Constants.LONG);
            ciudad = getArguments().getString(Constants.CIUDAD);
        }

        makeRequest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_weather, container, false);
//        MapFragment mapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.rl_map_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        listView= (ListView) view.findViewById(R.id.listWeather);



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void moveMap(ObjWeather infoWeather) {
        updateUI(infoWeather);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    private void ActualizarMapa(ObjWeather infoWeather){
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

//        url = "http://api.geonames.org/weatherJSON?north=40.65072578667785&south=40.18227840162254&east=-3.3938449928832797&west=-4.011283486120621&username=ilgeonamessample";

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ArrayList<ObjWeather> items = parseJson(jsonObject);
                adapter = new AdapterInfoWeather(getActivity().getApplicationContext(),R.layout.item_list_wether,items);
                adapter.notifyDataSetChanged();
                adapter.callback = MapWeatherFragment.this;
                listView.setAdapter(adapter);
                onConnectionFinished();
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

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("weatherObservations");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);


                    ObjWeather obj = new ObjWeather(objeto.getString("lng"),
                            objeto.getString("lat"),
                            objeto.getString("temperature"), objeto.getString("humidity"),
                            objeto.getString("stationName"), objeto.getString("weatherCondition"));

                    arrayObjWeather.add(obj);

                } catch (JSONException e) {
                    Log.e("ERROR", "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayObjWeather;
    }
}
