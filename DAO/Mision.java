/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Jair
 */
public class Mision {
    
    //fecha de la mision
    private Date fecha;
    
    //nombre de la mision
    private String nombre;
    
    //Descripcion relevante de la misiosn
    private String descripcion;
    
    //Aeronaves que estan en la mision
    private List<Aeronave> aeronaves;

    public Mision(Date fecha, String nombre, String descripcion) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.aeronaves = new ArrayList<>();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Aeronave> getAeronaves() {
        return aeronaves;
    }

    public void setAeronaves(List<Aeronave> aeronaves) {
        this.aeronaves = aeronaves;
    }
    
    public void agregarAeronave(Aeronave aeronave){
        this.aeronaves.add(aeronave);
    }        
}
