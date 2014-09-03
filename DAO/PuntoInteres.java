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
public class PuntoInteres implements Serializable{
    
    //breve descripcion del punto de interes
    private String descripcion;
    
    //describe geograficamente la posicion del punto de interes
    private Posicion posicion;
    
    //asignado por el comando o la aeronave
    private int asignacion; 
    
    public PuntoInteres() {
    }

    public PuntoInteres(String descripcion, Posicion posicion, int asignacion) {
        this.descripcion = descripcion;
        this.posicion = posicion;
        this.asignacion = asignacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(int asignacion) {
        this.asignacion = asignacion;
    }

    @Override
    public String toString() {
        return "PuntoInteres{" + "descripcion=" + descripcion + ", posicion=" + posicion + ", asignacion=" + asignacion + '}';
    }
    
    
    
}
