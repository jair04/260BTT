/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package SIG_ArcGIS;

import DAO.Aeronave;
import DAO.Posicion;
import com.esri.core.gps.GPSEventListener;
import com.esri.core.gps.GPSStatus;
import com.esri.core.gps.GeoPosition;
import com.esri.core.gps.Satellite;
import java.util.Map;


/**
 *
 * @author Jair
 */

//interfaz que nos permite manipular la informacion de las sentencias NMEA 0183
public class GeoPositionListener implements GPSEventListener {
    
    private final Aeronave aeronave;    
    
    public GeoPositionListener(Aeronave aeronave){
        this.aeronave = aeronave;
    }
    
    @Override
    public void onStatusChanged(GPSStatus gpss) {
    }

    @Override
    public void onNMEASentenceReceived(String string) {
    }
    
    @Override
    public void onSatellitesInViewChanged(Map<Integer, Satellite> map) {
    }
        
    @Override
    public void onPositionChanged(GeoPosition newPosition) {
        // display the location information in our text area  
        Posicion posicion = new Posicion(
                newPosition.getLocation().getLatitude()+"",
                newPosition.getLocation().getLongitude()+"",
                newPosition.getLocation().getAltitude()+"",
                newPosition.getLocation().getSpeed()+"",
                "mgrs");
        
        //se hace la actualizacion de las coordenas obtenidas del archivo
        this.aeronave.setPosicion(posicion);
        
        //Enviamos la informacion al comando central
        
        System.out.println("\n*********************************************"
                + "\n ipAeronave: " + aeronave.getIpAeronave()
                + "\n ipComando: "  + aeronave.getIpComando()
                + "\n Latitud: "    + newPosition.getLocation().getLatitude()
                + "\n Longitud: "   + newPosition.getLocation().getLongitude()
                + "\n Altitud: "    + newPosition.getLocation().getAltitude()
                + "\n Velocidad: "  + newPosition.getLocation().getSpeed()
                + "\n MGRS: "
        );

    }
  }
