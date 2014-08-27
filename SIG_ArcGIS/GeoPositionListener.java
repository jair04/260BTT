/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SIG_ArcGIS;

import GUI.General_GUI;
import GUI.InformationAirship;
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
import java.text.DecimalFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jair
 */
//interfaz que nos permite manipular la informacion de las sentencias NMEA 0183
public class GeoPositionListener implements GPSEventListener {

    JMap jMap;
    GraphicsLayer gpsLayer;
    General_GUI generalGUI;

    public GeoPositionListener(JMap jMap, GraphicsLayer gpsLayer, General_GUI generalGUI) {
        this.jMap = jMap;
        this.gpsLayer = gpsLayer;
        this.generalGUI = generalGUI;
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
        // Here we have to send the information to the central command  
        DecimalFormat decimal = new DecimalFormat("#.####");
        DecimalFormat decimalE = new DecimalFormat("#.#");

        if (newPosition != null) {   
            
            // Changing reference to mapPoint
            Point point = new Point(newPosition.getLocation().getLongitude(), newPosition.getLocation().getLatitude());
            Point mapPoint = (Point) GeometryEngine.project(point, SpatialReference.create(4326), jMap.getSpatialReference());

            //getting the panel from the General_GUI where is showing the updated information
            InformationAirship info = generalGUI.getControlPanel();
            
            //Showing the information in the jpanel
            info.getMgrsLabel().setText("MGRS: " + CoordinateConversion.pointToMgrs(mapPoint, jMap.getSpatialReference(), CoordinateConversion.MGRSConversionMode.AUTO, 3, true, true)+"         "); 
            info.getAlturaLabel().setText("Altura: " + getPixelElevationValue(mapPoint) + " mts.");
            info.getAltitudLabel().setText("Altitud: " + newPosition.getLocation().getAltitude() + " mts.");
            info.getLongitudLabel().setText("Longitud: " + decimal.format(newPosition.getLocation().getLongitude()));
            info.getLatitudLabel().setText("Latitud: " + decimal.format(newPosition.getLocation().getLatitude()));
            info.getVelocidadLabel().setText("Velocidad: " + decimal.format(newPosition.getLocation().getSpeed()) + " m/s");
            info.getElevacion().setText(decimalE.format(newPosition.getLocation().getAltitude()-Double.parseDouble(getPixelElevationValue(mapPoint)))+" mts.");
            
            //setting a dalay to allows the jpanel update correctly the information
            info.setVisible(false);
            try {
                info.setVisible(false);
                Thread.sleep(1);
                info.setVisible(true);
            } catch (InterruptedException ex) {
                Logger.getLogger(GeoPositionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    private String getPixelElevationValue(Geometry geometry) {
        String pixelValue="";
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
