package com.weather.diegojesuscampos.weather.Util;



import com.weather.diegojesuscampos.weather.Datos.ObjWeather;

import org.json.JSONObject;

/**
 * Constantes para escritura
 */
public class Constants {
    public static final String URLPLACE = "%PLACE%";
    public static final String URLUSERNAME = "%USERNAME%";
    public static final String URLNORTE = "%NORTE%";
    public static final String URLSUR = "%SUR%";
    public static final String URLESTE = "%ESTE%";
    public static final String URLOESTE = "%OESTE%";


    public static final String URL_INFO_GEOGRAFICA = "http://api.geonames.org/searchJSON?q="+URLPLACE+"&maxRows=20&startRow=0&lang=en&isNameRequired=true&style=FULL&username="+URLUSERNAME;
    public static final String URL_WEATHER = "http://api.geonames.org/weatherJSON?north="+URLNORTE+"&south="+URLSUR+"&east="+URLESTE+"&west="+URLOESTE+"&username="+URLUSERNAME;

     public static final String USERNAME1 = "ilgeonamessample";
    public static final String USERNAME2 = "demo";


    public static final String LAT = "latitud";
    public static final String LONG = "longuitud";
    public static final String CIUDAD = "ciudad";


    public static final String TAG_GEONAMES = "geonames";
    public static final String TAG_NORTE = "north";
    public static final String TAG_SUR = "south";
    public static final String TAG_ESTE = "east";
    public static final String TAG_OESTE = "west";
    public static final String TAG_LONGUITUD = "lng";
    public static final String TAG_COUNTRYNAME = "countryName";
    public static final String TAG_ADMINNAME2 = "adminName2";

    public static final String TAG_WEATHEROBSERVATION = "weatherObservations";
    public static final String TAG_TYPONAME = "toponymName";
    public static final String TAG_BBOX = "bbox";
    public static final String TAG_LATITUD = "LAT";
    public static final String TAG_TEMPERATURA = "temperature";
    public static final String TAG_HUMEDAD = "humidity";
    public static final String TAG_ESTACION = "stationName";
    public static final String TAG_CONDICIONCLIMA = "weatherCondition";

    public static final String TAG_BUSCARZONA = "BuscarZona";
    public static final String TAG_ZONABUSCADA = "ZonaBuscada";


}

