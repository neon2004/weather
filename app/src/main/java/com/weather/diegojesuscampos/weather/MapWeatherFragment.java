package com.weather.diegojesuscampos.weather;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapWeatherFragment extends BaseVolleyFragment  implements OnMapReadyCallback {
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
        args.putString(Constants.CIUDAD, objIngoGeo.getCiudad());
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
        MapFragment mapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

    }

    public void updateUI(Location loc) {
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 15);
        mapa.moveCamera(camUpd1);

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String zip = addresses.get(0).getPostalCode();
//        String country = addresses.get(0).getCountryName();

        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .title(ciudad));
    }


    private void makeRequest(){
        String url = Constants.URL_WEATHER;
        url = url.replace(Constants.URLPLACE,ciudad);
        url = url.replace(Constants.URLNORTE,norte);
        url = url.replace(Constants.URLSUR,sur);
        url = url.replace(Constants.URLESTE,este);
        url = url.replace(Constants.URLOESTE,oeste);
        url = url.replace(Constants.URLUSERNAME,Constants.USERNAME1);

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                label.setText(jsonObject.toString());
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
    }
}
