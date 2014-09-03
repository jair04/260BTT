/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Jair
 */
public class Mensaje implements Serializable{
   
    //it is used when the aiship send the actualization of its position 
    private Aeronave aeronave;
   
    //message type
    private int tipo;
    
    //message descripcino 
    private String descripcion;
    
    //this variable is used when the airship receive the the reply result about the connection to the central command
    private boolean peticionAceptada;
    
    //variable to send the mision´s updated information to the airship 
    private HashMap<String,Aeronave> mision = new HashMap<>();
    
    public Mensaje(){
            this.peticionAceptada = false;
            this.tipo = 0;
            this.descripcion = "";
            this.mision = null;
    }    

    public Mensaje(int tipo,Aeronave aeronave) {
        this.tipo = tipo;
        this.aeronave = aeronave;
        this.descripcion = null;
    }

    public Aeronave getAeronave() {
        return aeronave;
    }

    public void setAeronave(Aeronave aeronave) {
        this.aeronave = aeronave;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isPeticionAceptada() {
        return peticionAceptada;
    }

    public void setPeticionAceptada(boolean peticionAceptada) {
        this.peticionAceptada = peticionAceptada;
    }

    public HashMap<String, Aeronave> getMision() {
        return mision;
    }

    public void setMision(HashMap<String, Aeronave> mision) {
        this.mision = mision;
    }

    public Aeronave getAeronaveMision(String key){
        return this.mision.get(key);
    }
    
    public void addAeronaveMision(Aeronave aeronave){
        this.mision.put(aeronave.getMatricula(), aeronave);
    }
    
}
