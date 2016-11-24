package com.weather.diegojesuscampos.weather.Datos;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.diegojesuscampos.weather.R;

/**
 * Created by DJ on 23/11/2016.
 */

public class ZonasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewCiudad;
    public TextView textViewPais;
    public LinearLayout layout;
    public ObjInfoGeografica objInfoZona;


    public MyViewHolderClickListener mListener;

    private String TAG= getClass().getSimpleName();

    public static interface MyViewHolderClickListener{

        public void onItemClick(ObjInfoGeografica objInfo);
//        public void onTextViewNameClick(View view, int position);
//        public void onTextViewRollClick(View view, int position);
    }

    public ZonasViewHolder(View itemView) {
        super(itemView);
        textViewCiudad = (TextView) itemView.findViewById(R.id.txtCiudad);
        textViewPais = (TextView) itemView.findViewById(R.id.txtPais);
        layout = (LinearLayout) itemView.findViewById(R.id.layoutBuscarZona);


        layout.setOnClickListener(this);
//        textViewCiudad.setOnClickListener(this);
//        textViewPais.setOnClickListener(this);
    }


    public void bindToStudent(ObjInfoGeografica objInfo){
        if (objInfo == null) {
            return;
        }

        objInfoZona = objInfo;
        textViewCiudad.setText(objInfo.getCiudad());
        textViewPais.setText(objInfo.getPais());
    }

    public void setCustomOnClickListener(MyViewHolderClickListener listener  ){
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
//                case R.id.txtCiudad:
//                    mListener.onTextViewNameClick(view, getAdapterPosition());
//                    break;
//                case R.id.txtPais:
//                    mListener.onTextViewRollClick(view, getAdapterPosition());
//                    break;

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