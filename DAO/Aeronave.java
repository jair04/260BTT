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
public class Aeronave {
    
    //informacion general
    private String ip;
    private String matricula;
    private Piloto piloto;
    
    /*Almacena informacion geografica de una aeronave en un punto especifivo*/
    private Posicion posicion;
    
    //variable que almacena el tipo de peticion
    private int tipo_peticion;
    
    /*Mensaje que sera enviado de la aeronave al comando central */
    private Mensaje mensaje;

       
    public Aeronave() {
    }

    public Aeronave(String ip, String matricula, Piloto piloto, int tipo_peticion) {
        this.ip = ip;
        this.matricula = matricula;
        this.piloto = piloto;
        this.tipo_peticion = tipo_peticion;
    }

    public Aeronave(String ip, String matricula, Piloto piloto, Posicion posicion, int tipo_peticion, Mensaje mensaje) {
        this.ip = ip;
        this.matricula = matricula;
        this.piloto = piloto;
        this.posicion = posicion;
        this.tipo_peticion = tipo_peticion;
        this.mensaje = mensaje;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getTipo_peticion() {
        return tipo_peticion;
    }

    public void setTipo_peticion(int tipo_peticion) {
        this.tipo_peticion = tipo_peticion;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    } 
}
