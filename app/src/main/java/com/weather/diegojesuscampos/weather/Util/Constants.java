package com.weather.diegojesuscampos.weather.Util;

import com.google.android.gms.location.DetectedActivity;

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


    //    public static final String URL_INFO_GEOGRAFICA = "http://api.geonames.org/searchJSON?q="+URLPLACE+"&amp;maxRows=20&amp;startRow=0&amp;lang=en&amp;isNameRequired=true&amp;style=FULL&amp;username="+URLUSERNAME;
//    public static final String URL_WEATHER = "http://api.geonames.org/weatherJSON?north="+URLNORTE+"&amp;south="+URLSUR+"&amp;east="+URLESTE+"&amp;west="+URLOESTE+"&amp;username="+URLUSERNAME;
    public static final String USERNAME1 = "ilgeonamessample";
    public static final String USERNAME2 = "demo";


    public static final String NORTE = "norte";
    public static final String SUR = "sur";
    public static final String ESTE = "este";
    public static final String OESTE = "oeste";
    public static final String LAT = "latitud";
    public static final String LONG = "longuitud";
    public static final String CIUDAD = "ciudad";


}

