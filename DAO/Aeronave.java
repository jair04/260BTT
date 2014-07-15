/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import Controller.Constante;
import JMS_ActiveMQ.Consumidor;
import JMS_ActiveMQ.Publicador;
import SIG_ArcGIS.GeoPositionListener;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.GPSEventListener;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.IGPSWatcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.jms.JMSException;

/**
 *
 * @author Jair
 */
public class Aeronave implements Serializable{
    
    //informacion general
    private String ipAeronave;
    private String ipComando;
    private String matricula;
    private Piloto piloto;
    
    /*Almacena informacion geografica de una aeronave en un punto especifivo*/
    private Posicion posicion;
    
    /*Puntos de interes que han sido agregador*/
    List<PuntoInteres> puntosInteres;
    
    //tema en el cual se publicaran los mensajes
    private String tema;

    public Aeronave(String ipAeronave, String ipComando, String matricula, Piloto piloto, Posicion posicion, List<PuntoInteres> puntosInteres, String tema) {
        this.ipAeronave = ipAeronave;
        this.ipComando = ipComando;
        this.matricula = matricula;
        this.piloto = piloto;
        this.posicion = posicion;
        this.puntosInteres = puntosInteres;
        this.tema = tema;
    }

       
    public Aeronave() {
    }

    public Aeronave(String ipAeronave,String ipComando, String matricula, Piloto piloto) {
        this.ipAeronave = ipAeronave;
        this.ipComando = ipComando;
        this.matricula = matricula;
        this.piloto = piloto;
        this.puntosInteres = new ArrayList<>();
    }

    public Aeronave(String ipAeronave,String ipComando, String matricula, Piloto piloto, Posicion posicion) {
        this.ipAeronave = ipAeronave;
        this.ipComando = ipComando;
        this.matricula = matricula;
        this.piloto = piloto;
        this.posicion = posicion;
        this.puntosInteres = new ArrayList<>();
    }

    public String getIpAeronave() {
        return ipAeronave;
    }

    public void setIpAeronave(String ipAeronave) {
        this.ipAeronave = ipAeronave;
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

    public String getIpComando() {
        return ipComando;
    }

    public void setIpComando(String ipComando) {
        this.ipComando = ipComando;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
    
    @Override
    public String toString() {
        return "Aeronave{" + "ip=" 
                           + ipAeronave 
                           + ", matricula=" 
                           + matricula 
                           + ", piloto=" 
                           + piloto 
                           + ", posicion=" 
                           + posicion 
                           + ", tipo_peticion=" 
                           + '}';
    }

    @Override
    public int hashCode() {
        return this.ipAeronave.hashCode()+this.matricula.hashCode(); 
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aeronave other = (Aeronave) obj;
        if (!Objects.equals(this.ipAeronave, other.ipAeronave)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        return true;
    }

    public void realizarConexion() throws JMSException{
 }
    
    //lee sentencias NMEA 0183 de archivo TXT y las envia al comando central
    public void enviarInformacion() throws GPSException{    
        //Ruta absoluta del archivo
        String ruta = getClass().getResource("/Archivos/GPS/GPSReader.txt").toString();
        ruta = ruta.substring(6,ruta.length());
        
        /*
            Lectura del GPS y envio de la informacion actualizada
            al comando central a través de "Interfaz:GeoPositionListener.java"
        */
        GPSEventListener gpsListener = new GeoPositionListener(this);
        IGPSWatcher gpsWatcher = new FileGPSWatcher(ruta,500,false,gpsListener);
        gpsWatcher.start();
    }
    
    //leer GPS desde puerto COM
    public void leerGPSCOM(){
    
    }
    
    //lee la informacion que publica el comando central
    public Aeronave recibirInfoComando(){
        return null;
    }
    
    private String obtenerIP() throws MalformedURLException, IOException{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        return in.readLine();
    }
}
