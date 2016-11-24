package com.weather.diegojesuscampos.weather.Controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weather.diegojesuscampos.weather.Adapter.AdapterListZonasBuscadas;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.Datos.ZonasViewHolder;
import com.weather.diegojesuscampos.weather.Interfaces.IMyViewHolderClickListener;
import com.weather.diegojesuscampos.weather.R;
import com.weather.diegojesuscampos.weather.Util.Constants;


public class ZonasBuscadasFragment extends BaseVolleyFragment {


    private AdapterListZonasBuscadas mAdapter;
    private RecyclerView recycler;
    private DatabaseReference dbZonas;

    public ZonasBuscadasFragment() {
        // Required empty public constructor
    }


    public static ZonasBuscadasFragment newInstance() {
        ZonasBuscadasFragment fragment = new ZonasBuscadasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void crearRefeenciaBD() {

        dbZonas = FirebaseDatabase.getInstance().getReference().child(Constants.TAG_NOMBREBD);

        mAdapter = new AdapterListZonasBuscadas(getActivity(), ObjInfoGeografica.class, R.layout.item_list_busqueda, ZonasViewHolder.class, dbZonas);
        mAdapter.setMyViewHolderClickListener(new IMyViewHolderClickListener() {
            @Override
            public void onItemClick(ObjInfoGeografica objInfo) {
                MainActivity act = (MainActivity) getActivity();
                act.changeFragment(objInfo,Constants.TAG_VERCLIMA);
            }

            @Override
            public void onDeleteClick(ObjInfoGeografica objInfo) {
                dbZonas.child(objInfo.getId()).removeValue();
            }
        });

        recycler.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zonas_buscadas, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.lstZonas);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        crearRefeenciaBD();

        return view;
    }

   @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
//      dbZonas.removeEventListener(eventListener);
    }
}
