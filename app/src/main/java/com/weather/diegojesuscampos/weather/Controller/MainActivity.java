package com.weather.diegojesuscampos.weather.Controller;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.weather.diegojesuscampos.weather.Datos.ObjInfoGeografica;
import com.weather.diegojesuscampos.weather.R;
import com.weather.diegojesuscampos.weather.Util.VolleyS;

public class MainActivity extends AppCompatActivity implements   GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks   {

    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private RequestQueue fRequestQueue;
    private VolleyS volley;
    private MapWeatherFragment mapWeatherFragment;
    private BuscarLugarFragment buscarLugarFragment;
    private ZonasBuscadasFragment zonaBuscadaFragment;
    public static Fragment FRAGMENT_ACT = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapWeatherFragment = MapWeatherFragment.newInstance();
        buscarLugarFragment = BuscarLugarFragment.newInstance();
        zonaBuscadaFragment  = ZonasBuscadasFragment.newInstance();

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            buscarLugarFragment = BuscarLugarFragment.newInstance(lastLocation);
            FRAGMENT_ACT = buscarLugarFragment;
            // CARGAMOS EL FRAGMENT DONDE MOSTRAR EL TIEMPO Y EL MAPA DE LA LOCALIZACION
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contenedor, buscarLugarFragment);
            transaction.commit();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);
                buscarLugarFragment = BuscarLugarFragment.newInstance(lastLocation);
                FRAGMENT_ACT = buscarLugarFragment;
                // CARGAMOS EL FRAGMENT DONDE MOSTRAR EL TIEMPO Y EL MAPA DE LA LOCALIZACION
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.contenedor, buscarLugarFragment);
                transaction.commit();

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    // CAMBIAMOS EL FRAGMENT A MOSTRAR
    public void changeFragment(ObjInfoGeografica infoPlace,String framgenCargar){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        switch (framgenCargar) {
            case "Weather":
                mapWeatherFragment = MapWeatherFragment.newInstance(infoPlace);
                FRAGMENT_ACT = mapWeatherFragment;
                transaction.replace(R.id.contenedor, mapWeatherFragment);

                transaction.commit();
                break;
            case "BuscarZona":
                FRAGMENT_ACT = buscarLugarFragment;
                transaction.replace(R.id.contenedor, buscarLugarFragment);
                transaction.commit();
                break;
            case "ZonaBuscada":
                FRAGMENT_ACT = zonaBuscadaFragment;
                transaction.replace(R.id.contenedor, zonaBuscadaFragment);
                transaction.commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buscarZonas:
                changeFragment(null,"BuscarZona");
                return true;
            case R.id.zonasBuscadas:
                changeFragment(null,"ZonaBuscada");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
