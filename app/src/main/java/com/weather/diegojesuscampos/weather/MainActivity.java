package com.weather.diegojesuscampos.weather;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements   GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;
    private LocationRequest locRequest;

    private RequestQueue fRequestQueue;
    private VolleyS volley;
    private MapWeatherFragment mapWeatherFragment;
    private BuscarLugarFragment buscarLugarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapWeatherFragment = MapWeatherFragment.newInstance();



// CARGAMOS EL FRAGMENT DONDE MOSTRAR EL TIEMPO Y EL MAPA DE LA LOCALIZACION
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contenedor, buscarLugarFragment);
        transaction.commit();


//        lblLatitud = (TextView) findViewById(R.id.lblLatitud);
//        lblLongitud = (TextView) findViewById(R.id.lblLongitud);
//        btnActualizar = (ToggleButton) findViewById(R.id.btnActualizar);
//        btnActualizar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleLocationUpdates(btnActualizar.isChecked());
//            }
//        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
    }

//    public void addToQueue(Request request) {
//        if (request != null) {
//            request.setTag(this);
//            if (fRequestQueue == null)
//                fRequestQueue = volley.getRequestQueue();
//            request.setRetryPolicy(new DefaultRetryPolicy(
//                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            ));
//            onPreStartConnection();
//            fRequestQueue.add(request);
//        }
//    }

//    private void toggleLocationUpdates(boolean enable) {
//        if (enable) {
//            enableLocationUpdates();
//        } else {
//            disableLocationUpdates();
//        }
//    }
//
//    private void enableLocationUpdates() {
//
//        locRequest = new LocationRequest();
//        locRequest.setInterval(2000);
//        locRequest.setFastestInterval(1000);
//        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationSettingsRequest locSettingsRequest =
//                new LocationSettingsRequest.Builder()
//                        .addLocationRequest(locRequest)
//                        .build();
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(
//                        apiClient, locSettingsRequest);
//
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult locationSettingsResult) {
//                final Status status = locationSettingsResult.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//
//                        Log.i(LOGTAG, "Configuración correcta");
//                        startLocationUpdates();
//
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        try {
//                            Log.i(LOGTAG, "Se requiere actuación del usuario");
//                            status.startResolutionForResult(MainActivity.this, PETICION_CONFIG_UBICACION);
//                        } catch (IntentSender.SendIntentException e) {
//                            btnActualizar.setChecked(false);
//                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
//                        }
//
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
//                        btnActualizar.setChecked(false);
//                        break;
//                }
//            }
//        });
//    }

//    private void disableLocationUpdates() {
//
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                apiClient, this);
//
//    }

//    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
//            //Sería recomendable implementar la posible petición en caso de no tenerlo.
//
//            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");
//
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    apiClient, locRequest, MainActivity.this);
//        }
//    }

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

//            mapWeatherFragment.updateUI(lastLocation);
//            updateUI(lastLocation);
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
                mapWeatherFragment.updateUI(lastLocation);
//                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case PETICION_CONFIG_UBICACION:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        startLocationUpdates();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
//                        btnActualizar.setChecked(false);
//                        break;
//                }
//                break;
//        }
//    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(LOGTAG, "Recibida nueva ubicación!");

        mapWeatherFragment.updateUI(location);
        //Mostramos la nueva ubicación recibida
//        updateUI(location);
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        apiClient.connect();
        AppIndex.AppIndexApi.start(apiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(apiClient, getIndexApiAction());
        apiClient.disconnect();
    }
}
