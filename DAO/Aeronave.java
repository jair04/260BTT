/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import ArcGIS.GeoPositionListener;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.GPSEventListener;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.IGPSWatcher;
import java.util.Objects;

/**
 *
 * @author Jair
 */
public class Aeronave {
    
    //informacion general
    private String ipAeronave;
    private String ipComando;
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

    public Aeronave(String ipAeronave,String ipComando, String matricula, Piloto piloto, int tipo_peticion) {
        this.ipAeronave = ipAeronave;
        this.ipComando = ipComando;
        this.matricula = matricula;
        this.piloto = piloto;
        this.tipo_peticion = tipo_peticion;
    }

    public Aeronave(String ipAeronave,String ipComando, String matricula, Piloto piloto, Posicion posicion, int tipo_peticion, Mensaje mensaje) {
        this.ipAeronave = ipAeronave;
        this.ipComando = ipComando;
        this.matricula = matricula;
        this.piloto = piloto;
        this.posicion = posicion;
        this.tipo_peticion = tipo_peticion;
        this.mensaje = mensaje;
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

    @Override
    public String toString() {
        return "Aeronave{" + "ip=" + ipAeronave + ", matricula=" + matricula + ", piloto=" + piloto + ", posicion=" + posicion + ", tipo_peticion=" + tipo_peticion + ", mensaje=" + mensaje + '}';
    }

    @Override
    public int hashCode() {
        return this.ipAeronave.hashCode()+this.matricula.hashCode(); //To change body of generated methods, choose Tools | Templates.
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
    
    //leer sentencia NMEA 0183 de archivo TXT
    public void leerGPSTXT() throws GPSException{
        GPSEventListener gpsListener = new GeoPositionListener(this);
        IGPSWatcher gpsWatcher = new FileGPSWatcher("C:\\Users\\Jair\\Documents\\NetBeansProjects\\SistemaTactico_Repositorio\\Archivos\\GPS\\GPSReader.txt"
                ,500,true,gpsListener);
        gpsWatcher.start();
    }
    
    //leer GPS desde puerto COM
    public void leerGPSCOM(){
    
    }
    
    
}
