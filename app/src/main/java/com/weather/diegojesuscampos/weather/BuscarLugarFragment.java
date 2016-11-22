package com.weather.diegojesuscampos.weather;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class BuscarLugarFragment extends BaseVolleyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private String ciudad;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getString(Constants.LAT);
            mParam2 = getArguments().getString(Constants.LONG);

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            List<Address> addresses  = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String city = addresses.get(0).getLocality();


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_lugar, container, false);
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
