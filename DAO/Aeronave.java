/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import SIG_ArcGIS.GeoPositionListener;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.GPSEventListener;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.IGPSWatcher;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class Aeronave implements Serializable {

    /*Personal information about airship pilot*/
    private String matricula;
    private Piloto piloto;

    /*Almacena informacioni geografica de una aeronave en un punto especifivo*/
    private Posicion posicion;
    
    private Color color;

    /*Puntos de interes que han sido agregador*/
    List<PuntoInteres> puntosInteres;

    public Aeronave() {
        this.puntosInteres = new ArrayList<>();
    }

    public Aeronave(String matricula, Piloto piloto, Posicion posicion) {
        this.matricula = matricula;
        this.piloto = piloto;
        this.posicion = posicion;
        this.puntosInteres = new ArrayList<>();
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

    public void setPuntoInteres(PuntoInteres puntoInteres) {
        this.puntosInteres.add(puntoInteres);
    }

    public List<PuntoInteres> getPuntosInteres() {
        return this.puntosInteres;
    }

    public void setPuntosInteres(List<PuntoInteres> puntosInteres) {
        this.puntosInteres = puntosInteres;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    

    @Override
    public String toString() {
        return "Aeronave{" + ", matricula="
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
        return this.matricula.hashCode();
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
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        return true;
    }

    public void realizarConexion() throws JMSException {
    }

    //lee sentencias NMEA 0183 de archivo TXT y las envia al comando central
    public void enviarInformacion() throws GPSException {

        //Ruta absoluta del archivo
        String ruta = getClass().getResource("/Archivos/GPS/GPSReader.txt").toString();
        ruta = ruta.substring(6, ruta.length());

        /*
         Lectura del GPS y envio de la informacion actualizada
         al comando central a través de "Interfaz:GeoPositionListener.java"
         */
        /*
         GPSEventListener gpsListener = new GeoPositionListener(this);
         IGPSWatcher gpsWatcher = new FileGPSWatcher(ruta,500,false,gpsListener);
         gpsWatcher.start();*/
    }

    public void readFileInformation() throws FileNotFoundException, IOException {
        //current directory
        final String currentPath = new java.io.File(".").getCanonicalPath();
        

        try (BufferedReader br = new BufferedReader(new FileReader(currentPath + "\\build\\classes\\Archivos\\Datos\\informacionPersonal.txt"))) {

            Piloto pilotoAux = new Piloto();
            
            String line;
            
            line = br.readLine();
            pilotoAux.setMatricula(line);

            line = br.readLine();
            pilotoAux.setNombre(line);
            
            line = br.readLine();
            pilotoAux.setApellidoPaterno(line);
            
            line = br.readLine();
            pilotoAux.setApellidoMaterno(line);
            
            line = br.readLine();
            pilotoAux.setEdad(Integer.parseInt(line));
            
            line = br.readLine();
            this.setMatricula(line);
            
            this.setPiloto(pilotoAux);           
        }
    }

    private String obtenerIP() throws MalformedURLException, IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        return in.readLine();
    }
}
