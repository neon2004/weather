package com.weather.diegojesuscampos.weather.Datos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diego Jesus Campos on 22/11/2016.
 */

public class ObjInfoGeografica {
    private String ciudad;
    private String norte;
    private String sur;
    private String este;
    private String oeste;
    private String lng;
    private String lat;
    private String pais;
    private String lugar;
    private String id;

    public ObjInfoGeografica(){}

    public ObjInfoGeografica(String id, String lugar, String norte, String sur, String este, String oeste, String lng, String lat, String pais, String ciudad) {
        this.lugar = id;
        this.lugar = lugar;
        this.norte = norte;
        this.sur = sur;
        this.este = este;
        this.oeste = oeste;
        this.lng = lng;
        this.lat = lat;
        this.pais = pais;
        this.ciudad = ciudad;
    }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lugar", getId());
        result.put("lugar", getLugar());
        result.put("ciudad", getCiudad());
        result.put("pais", getPais());
        result.put("lat", getLat());
        result.put("lng", getLng());
        result.put("norte", getNorte());
        result.put("sur", getSur());
        result.put("este", getEste());
        result.put("oeste", getOeste());


        return result;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getNorte() {
        return norte;
    }

    public void setNorte(String norte) {
        this.norte = norte;
    }

    public String getSur() {
        return sur;
    }

    public void setSur(String sur) {
        this.sur = sur;
    }

    public String getEste() {
        return este;
    }

    public void setEste(String este) {
        this.este = este;
    }

    public String getOeste() {
        return oeste;
    }

    public void setOeste(String oeste) {
        this.oeste = oeste;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
