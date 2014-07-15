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
public class Piloto extends Persona implements Serializable{
    private String matricula;

    public Piloto() {
        super();
        this.matricula=null;
    }

    public Piloto(String matricula, String nombre, String apellidoPaterno, String apellidoMaterno, int edad) {
        super(nombre, apellidoPaterno, apellidoMaterno, edad);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    
    
}
