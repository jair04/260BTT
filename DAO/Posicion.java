/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.io.Serializable;

/**
 *
 * @author Jair
 */

//Almacena la informacion geografica de la aeronave en un punto especifico
public class Posicion implements Serializable{
    
    //Variables que cambian dependiendo de la posicion 
    private String latitud;
    private String longitud;
    private String altitud;
    
    //coordenadas en sistema militar Military Grid Reference Sistem 
    private String mgrs;
    
    //Velocidad en un punto especifico
    private String velocidad;


    public Posicion() {
    }

    public Posicion(String latitud, String longitud, String altitud, String velocidad, String mgrs) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.velocidad = velocidad;
        this.mgrs = mgrs;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getMgrs() {
        return mgrs;
    }

    public void setMgrs(String mgrs) {
        this.mgrs = mgrs;
    }
    
    
}
