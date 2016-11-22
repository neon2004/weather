package com.weather.diegojesuscampos.weather;

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

    public ObjInfoGeografica(String ciudad, String norte, String sur, String este, String oeste, String lng, String lat, String pais) {
        this.ciudad = ciudad;
        this.norte = norte;
        this.sur = sur;
        this.este = este;
        this.oeste = oeste;
        this.lng = lng;
        this.lat = lat;
        this.pais = pais;
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
}
