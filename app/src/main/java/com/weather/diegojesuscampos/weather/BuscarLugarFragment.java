package com.weather.diegojesuscampos.weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class BuscarLugarFragment extends BaseVolleyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextInputLayout tilCiudad;
    private AdapterBusquedaLugar adapter;

    private static BuscarLugarFragment mInstance;

    private ListView listView;
    private String ciudad;


    // TODO: Rename and change types of parameters
    private Double lat;
    private Double lng;

    private OnFragmentInteractionListener mListener;

    public BuscarLugarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BuscarLugarFragment newInstance(Location lastLocation) {
        BuscarLugarFragment fragment = new BuscarLugarFragment();
        Bundle args = new Bundle();
        args.putDouble(Constants.LAT, lastLocation.getLatitude());
        args.putDouble(Constants.LONG, lastLocation.getLongitude());
        fragment.setArguments(args);
        return fragment;
    }

    public static BuscarLugarFragment newInstance() {
        BuscarLugarFragment fragment = new BuscarLugarFragment();

        if(mInstance == null){
            mInstance = fragment;
        }
        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            lat = getArguments().getDouble(Constants.LAT);
            lng = getArguments().getDouble(Constants.LONG);

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            List<Address> addresses  = null;
            try {
                addresses = geocoder.getFromLocation(lat,lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ciudad = addresses.get(0).getLocality();
            tilCiudad.getEditText().setText(ciudad);
            validarNombre(tilCiudad.getEditText().getText().toString());
        }

        tilCiudad.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&   (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    validarNombre(tilCiudad.getEditText().getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_lugar, container, false);

        tilCiudad = (TextInputLayout) view.findViewById(R.id.til_ciudad);
        listView= (ListView) view.findViewById(R.id.listView);



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

    private void makeRequest(){
        String url = Constants.URL_INFO_GEOGRAFICA;
        url = url.replace(Constants.URLPLACE,ciudad);
        url = url.replace(Constants.URLUSERNAME,Constants.USERNAME1);

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                label.setText(jsonObject.toString());
                ArrayList<ObjInfoGeografica> items = parseJson(jsonObject);
                adapter = new AdapterBusquedaLugar(getActivity().getApplicationContext(),R.layout.item_list_busqueda,items);
                adapter.mInstance = mInstance;
                listView.setAdapter(adapter);

                listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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

    private void validarNombre(String nombre){
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if( patron.matcher(nombre).matches() || nombre.length() > 5){
            makeRequest();
        }
    }

    public ArrayList<ObjInfoGeografica> parseJson(JSONObject jsonObject){
        // Variables locales
        ArrayList<ObjInfoGeografica> arrayObjInfGeo = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("geonames");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    JSONObject puntosCardenale = objeto.getJSONObject("bbox");

                    ObjInfoGeografica obj = new ObjInfoGeografica(objeto.getString("toponymName"),
                            puntosCardenale.getString("north"),
                            puntosCardenale.getString("south"), puntosCardenale.getString("east"),
                            puntosCardenale.getString("west"), objeto.getString("lat"),
                            objeto.getString("lng") , objeto.getString("countryName"));

                    arrayObjInfGeo.add(obj);

                } catch (JSONException e) {
                    Log.e("ERROR", "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayObjInfGeo;
    }

    public void showWeatherPlace(){

    }
}
