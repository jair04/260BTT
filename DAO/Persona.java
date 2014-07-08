/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

/**
 *
 * @author Jair
 */
public class Persona {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int edad;

    
    public Persona() {
        this.nombre = null;
        this.apellidoPaterno = null;
        this.apellidoMaterno = null;
        this.edad = 0;
    }
    
    public Persona(String nombre, String apellidoPaterno, String apellidoMaterno, int edad) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public int getEdad() {
        return edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    
}
