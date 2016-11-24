package com.weather.diegojesuscampos.weather.Datos;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.diegojesuscampos.weather.Interfaces.IMyViewHolderClickListener;
import com.weather.diegojesuscampos.weather.R;

/**
 * Created by DJ on 23/11/2016.
 */

public class ZonasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewCiudad;
    public TextView textViewPais;
    public LinearLayout layout;
    public ObjInfoGeografica objInfoZona;


    public IMyViewHolderClickListener mListener;

    private String TAG= getClass().getSimpleName();

    public ZonasViewHolder(View itemView) {
        super(itemView);
        textViewCiudad = (TextView) itemView.findViewById(R.id.txtCiudad);
        textViewPais = (TextView) itemView.findViewById(R.id.txtPais);
        layout = (LinearLayout) itemView.findViewById(R.id.layoutBuscarZona);
        ImageButton btnEliminar = (ImageButton) itemView.findViewById(R.id.btnEliminar);
        btnEliminar.setVisibility(View.VISIBLE);
        layout.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

    }


    public void bindToStudent(ObjInfoGeografica objInfo){
        if (objInfo == null) {
            return;
        }

        objInfoZona = objInfo;
        textViewCiudad.setText(objInfo.getLugar()+", "+objInfo.getCiudad());
        textViewPais.setText(objInfo.getPais());
    }

    public void setCustomOnClickListener(IMyViewHolderClickListener listener  ){
        this.mListener = listener;
    }

    @Override
    public void onClick(View view) {

        Log.d(TAG, "onClick at " + getAdapterPosition());
        Log.d(TAG, "id: " + view.getId());

        if( mListener!= null ){
            switch (view.getId()) {
                case R.id.layoutBuscarZona:
                    mListener.onItemClick(objInfoZona);
                    break;
                case R.id.btnEliminar:
                    mListener.onDeleteClick(objInfoZona);
                    break;
                default:
                    break;
            }
        }
    }
}













//public class ZonasViewHolder extends RecyclerView.ViewHolder {
//    private View mView;
//
//    public ZonasViewHolder(View itemView) {
//        super(itemView);
//        mView = itemView;
//    }
//
//    public void setLugar(String lugar) {
//        TextView field = (TextView) mView.findViewById(R.id.txtCiudad);
//        field.setText(lugar);
//    }
//
//    public void setPais(String pais) {
//        TextView field = (TextView) mView.findViewById(R.id.txtPais);
//        field.setText(pais);
//    }
//}