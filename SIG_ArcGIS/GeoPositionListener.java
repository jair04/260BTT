/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG_ArcGIS;

import Controller.Constante;
import DAO.Aeronave;
import DAO.Mensaje;
import DAO.Posicion;
import DAO.PuntoInteres;
import GUI.Aeronave_GUI;
import GUI.General_GUI;
import GUI.InformationAirship;
import JMS_ActiveMQ.Consumidor;
import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.gps.GPSEventListener;
import com.esri.core.gps.GPSStatus;
import com.esri.core.gps.GeoPosition;
import com.esri.core.gps.Satellite;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.identify.IdentifyTask;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.swing.JPanel;

/**
 *
 * @author Jair
 */
//interfaz que nos permite manipular la informacion de las sentencias NMEA 0183
public class GeoPositionListener implements GPSEventListener {

    JMap jMap;
    GraphicsLayer gpsLayer;
    General_GUI generalGUI;
    Aeronave_GUI aeronaveGUI;

    Consumidor consumidor;
    JMap map;

    public GeoPositionListener(General_GUI generalGUI) {

        this.generalGUI = generalGUI;
        this.aeronaveGUI = (Aeronave_GUI) generalGUI;

        this.jMap = generalGUI.getMap();
        this.gpsLayer = generalGUI.getGpsLayer();
        this.consumidor = aeronaveGUI.getConsumidor();
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
        //setting format to the showing number
        DecimalFormat decimal = new DecimalFormat("#.####");
        DecimalFormat decimalE = new DecimalFormat("#.#");

        if (newPosition != null) {
            // Changing reference to mapPoint
            Point point = new Point(newPosition.getLocation().getLongitude(), newPosition.getLocation().getLatitude());
            Point mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), jMap.getSpatialReference());          
            
            //getting the panel from the General_GUI where is showing the updated information
            InformationAirship info = generalGUI.getControlPanel();            

            String mgrs = CoordinateConversion.pointToMgrs(mapPoint, jMap.getSpatialReference(), CoordinateConversion.MGRSConversionMode.AUTO, 3, true, true);
            String altura = getPixelElevationValue(mapPoint);
            String altitud = newPosition.getLocation().getAltitude() + "";
            String longitud = decimal.format(newPosition.getLocation().getLongitude());
            String latitud = decimal.format(newPosition.getLocation().getLatitude());
            String velocidad = decimal.format(newPosition.getLocation().getSpeed());

            //Showing the information in the jpanel
            info.getMgrsLabel().setText("MGRS: " + mgrs + "         ");
            info.getAlturaLabel().setText("Altura: " + altura + " mts.");
            info.getAltitudLabel().setText("Altitud: " + altitud + " mts.");
            info.getLongitudLabel().setText("Longitud: " + longitud);
            info.getLatitudLabel().setText("Latitud: " + latitud);
            info.getVelocidadLabel().setText("Velocidad: " + velocidad + " m/s");
            info.getElevacion().setText(decimalE.format(newPosition.getLocation().getAltitude() - Double.parseDouble(getPixelElevationValue(mapPoint))) + " mts.");

            //Sending data to central command for being updated 
            if (this.consumidor.getMensaje().isPeticionAceptada()) {
                Posicion updatedPosition = new Posicion(latitud, longitud, altitud, velocidad, mgrs, altura);
                Aeronave aeronaveUpdated = new Aeronave();
                try {
                    aeronaveUpdated.readFileInformation();
                    aeronaveUpdated.setPosicion(updatedPosition);
                    
                    aeronaveUpdated.setPuntosInteres(generalGUI.getPuntosInteres());
                    generalGUI.setPuntosInteres(new ArrayList<PuntoInteres>());
                    
                    Mensaje updateData = new Mensaje(Constante.ACTUALIZAR_AERONAVE, aeronaveUpdated);
                    this.consumidor.sendMessage(updateData);
                } catch (IOException | JMSException | InterruptedException ex) {
                    Logger.getLogger(GeoPositionListener.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            generalGUI.getControlPanel().repaint();
        }
    }

    private String getPixelElevationValue(Geometry geometry) {
        String pixelValue = "";
        IdentifyParameters identifyparam = new IdentifyParameters();
        identifyparam.setGeometry(geometry);
        identifyparam.setMapExtent(jMap.getExtent());
        identifyparam.setSpatialReference(jMap.getSpatialReference());
        identifyparam.setMapHeight(jMap.getHeight());
        identifyparam.setMapWidth(jMap.getWidth());
        identifyparam.setLayerMode(IdentifyParameters.ALL_LAYERS);
        identifyparam.setDPI(Toolkit.getDefaultToolkit().getScreenResolution());

        IdentifyTask task = new IdentifyTask((jMap.getLayers().get(1)).getUrl());
        try {
            IdentifyResult[] results = task.execute(identifyparam);
            pixelValue = results(results);
        } catch (Exception e) {
        }

        return pixelValue;
    }

    private String results(IdentifyResult[] results) {

        String pixelValue = "";

        for (IdentifyResult result : results) {
            // if layer id = 3 (DEM layer): show attributes including pixel value (elevation in feet)
            for (Map.Entry<String, Object> attribute : result.getAttributes().entrySet()) {

                if (attribute.getKey().equals("Pixel Value") && !attribute.getValue().equals("NoData")) {
                    pixelValue = attribute.getValue().toString();
                }

            }
        }
        return pixelValue;
    }

}
