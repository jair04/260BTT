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
public class Mensaje implements Serializable{
   
    private Aeronave aeronave;
    private int tipo;
    private String descripcion;

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

}
