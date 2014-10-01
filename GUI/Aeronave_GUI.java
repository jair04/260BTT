/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.Constante;
import DAO.Aeronave;
import JMS_ActiveMQ.Consumidor;
import SIG_ArcGIS.GeoPositionListener;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.IGPSWatcher;
import com.esri.core.gps.SerialPortGPSWatcher;
import com.esri.map.GPSLayer;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jair
 */
public class Aeronave_GUI extends General_GUI {

    //private final JPanel gpsPanel = super.createButtonPanel("gpsOff", 100, 145);
    private final JScrollPane subGPS;
    private final String offset = "                                                            ";
    private SerialPortGPSWatcher gpsWatcher;
    private final Consumidor consumidor;

    public Aeronave_GUI() throws Exception {
        //Filling airchip general information 
        Aeronave aeronave = new Aeronave();
        aeronave.readFileInformation();
        
        super.setTypeUser(Constante.AERONAVE);
        super.getControlPanel().setTextTextField(aeronave.getMatricula());
        
        
        this.consumidor = new Consumidor("", aeronave, this);
        
        //GPS submenu
        subGPS = this.getGPS_PanelSubmenu();
        contentPane.add(subGPS);
    }

    @Override
    public JComponent createUI() {
        ButtonPanel gpsPanel = null;
        ButtonPanel conectServer = null;

        
        try {
            //ButtonPanel documents = new ButtonPanel("map", 10, 595, null);
            //gpsPanel = new ButtonPanel("gpsOff", 100, 145, subGPS);
            gpsPanel = new ButtonPanel("gpsOff", 10, 595, subGPS);
            
            //Here we send the consumer, then inside we sent the ip and establish the comunication 
            conectServer = new ButtonPanel("conectServer", 10, 145, null);
            conectServer.setConsumidor(this.consumidor);
        } catch (IOException ex) {
            Logger.getLogger(Aeronave_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        super.contentPane.add(conectServer);  
        super.contentPane.add(gpsPanel);
        super.contentPane.add(super.map);

        return super.contentPane;
    }

    /*
     *Submenus 
     */
    private JScrollPane getGPS_PanelSubmenu() {
        List<JComponent> components = new ArrayList<>();
        
        final JCheckBox navegacion = new JCheckBox("Navegación" + offset);
        navegacion.setSelected(true);
        navegacion.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!navegacion.isSelected()) {
                    gpsLayer.setMode(GPSLayer.Mode.NAVIGATION);
                } else {
                    gpsLayer.setMode(GPSLayer.Mode.OFF);
                }
            }
        });
        
        final JCheckBox estado = new JCheckBox("Activar" + offset);
        estado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    startGPS("COM7");
                } else {
                    stopGPS();
                }
            }
        });

        components.add(navegacion);
        components.add(estado);
        
        
        return new ScrollPanel(super.createSubmenu_panel(components, true), 95, 595);
    }

    //starting to read the NMEA senteces which were received from the serial port 
    protected void startGPS(String COMport) {
        //GPS layer
        try {
            GeoPositionListener myGeo = new GeoPositionListener(this);
            
            //SerialPortInfo myPortInfo = new SerialPortInfo("COM7", BaudRate.BAUD_9600, Parity.NONE, StopBits.ONE, 8);          
            //gpsWatcher = new SerialPortGPSWatcher(myPortInfo, myGeo);
  
             String path = new java.io.File(".").getCanonicalPath()+"\\build\\classes\\Archivos\\GPS\\GPSReader.txt";
            IGPSWatcher gpsWatcherFile = new FileGPSWatcher(path, 500, true, myGeo);
            
           
            
            super.gpsLayer = new GPSLayer(gpsWatcherFile);
            super.gpsLayer.setMode(GPSLayer.Mode.NAVIGATION);
            super.gpsLayer.setNavigationPointHeightFactor(0.5);
            
            super.layersList.add(super.gpsLayer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GPS no conectado");
        }
    }

    //stop reading gps NMEA sentences. 
    private void stopGPS(){
        try {
            gpsWatcher.stop();
        } catch (GPSException ex) {
            Logger.getLogger(Aeronave_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    

    public Consumidor getConsumidor() {
        return consumidor;
    }

    
    
}
