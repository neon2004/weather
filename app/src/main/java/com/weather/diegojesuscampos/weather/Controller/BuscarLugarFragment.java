package com.weather.diegojesuscampos.weather.Controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.weather.diegojesuscampos.weather.Adapter.AdapterBusquedaLugar;
import com.weather.diegojesuscampos.weather.Interfaces.IShowWeather;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.R;
import com.weather.diegojesuscampos.weather.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class BuscarLugarFragment extends BaseVolleyFragment implements IShowWeather {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextInputLayout tilCiudad;
    private TextInputEditText etCiudad;
    private AdapterBusquedaLugar adapter;

    private static BuscarLugarFragment mInstance;

    private ListView listView;
    private String ciudad;

    private Double lat;
    private Double lng;
    private DatabaseReference mDatabase;
    ArrayList<ObjInfoGeografica> items;


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

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_lugar, container, false);

        tilCiudad = (TextInputLayout) view.findViewById(R.id.til_ciudad);
        etCiudad = (TextInputEditText) view.findViewById(R.id.value_ciudad);
        listView= (ListView) view.findViewById(R.id.listView);

        if(ciudad != null && !ciudad.isEmpty()){
            tilCiudad.getEditText().setText(ciudad);
            validarCiudad(etCiudad.getText().toString());
        }

        etCiudad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validarCiudad(etCiudad.getText().toString());
                    return true;
                }
                return false;
            }
        });


//        etCiudad.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&   (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    // Perform action on key press
//                    validarCiudad(etCiudad.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
        
        //REFERENCIA A LA BD
        mDatabase = FirebaseDatabase.getInstance().getReference();

        items = new ArrayList<ObjInfoGeografica>();
        adapter = new AdapterBusquedaLugar(getActivity().getApplicationContext(),R.layout.item_list_busqueda,items);
        adapter.callback = BuscarLugarFragment.this;
        listView.setAdapter(adapter);

        return view;
    }


    @Override
    public void showWeatherPLaces(ObjInfoGeografica infoPlace) {

        almacenarZona(infoPlace);
        MainActivity act = (MainActivity) getActivity();
        act.changeFragment(infoPlace,"Weather");
    }

    private void almacenarZona(ObjInfoGeografica infoPlace) {
        String key = mDatabase.child("Zona").push().getKey();
        infoPlace.setId(key);
        Map<String, Object> postValues = infoPlace.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Zonas/" + key, infoPlace);

        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    Toast.makeText(getActivity(),"DATOS GUARDADOS",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"ERROR AL GUARDAR DATOS ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }



    private void makeRequest(){
        String url = Constants.URL_INFO_GEOGRAFICA;
        url = url.replace(Constants.URLPLACE,ciudad);
        url = url.replace(Constants.URLUSERNAME,Constants.USERNAME1);

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                label.setText(jsonObject.toString());
                items = parseJson(jsonObject);
                adapter.clear();//                adapter = new AdapterBusquedaLugar(getActivity().getApplicationContext(),R.layout.item_list_busqueda,items);
                adapter. updateObjInfoGeograficaList(items);
//                adapter.notifyDataSetChanged();

                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(
                        volleyError.toString());
            }
        });
        addToQueue(request);
    }

    private void validarCiudad(String nombre){
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if( patron.matcher(nombre).matches() && !TextUtils.isEmpty(nombre)){
            ciudad = nombre;
            makeRequest();
            tilCiudad.setError(null);
        }else{
            tilCiudad.setError("Es necesario introducir un lugar");
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

                    ObjInfoGeografica obj = new ObjInfoGeografica(i+"",objeto.getString("toponymName"),
                            puntosCardenale.getString("north"),
                            puntosCardenale.getString("south"), puntosCardenale.getString("east"),
                            puntosCardenale.getString("west"), objeto.getString("lat"),
                            objeto.getString("lng") , objeto.getString("countryName"), objeto.getString("adminName2"));

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


}
