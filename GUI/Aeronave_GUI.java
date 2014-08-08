/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import SIG_ArcGIS.GeoPositionListener;
import com.esri.core.gps.BaudRate;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.Parity;
import com.esri.core.gps.SerialPortGPSWatcher;
import com.esri.core.gps.SerialPortInfo;
import com.esri.core.gps.StopBits;
import com.esri.map.GPSLayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jair
 */
public class Aeronave_GUI extends General_GUI {

    //private final JPanel gpsPanel = super.createButtonPanel("gpsOff", 100, 145);
    private final JScrollPane subGPS;
    private final String offset = "                          ";
    private SerialPortGPSWatcher gpsWatcher;

    public Aeronave_GUI() throws Exception {
        //GPS submenu
        subGPS = this.getGPS_PanelSubmenu();
        contentPane.add(subGPS);
    }

    @Override
    public JComponent createUI() {
        ButtonPanel gpsPanel = null;
        ButtonPanel conectServer = null;

        
        try {
            gpsPanel = new ButtonPanel("gpsOff", 100, 145, subGPS);
            conectServer = new ButtonPanel("conectServer", 10, 145, subGPS);
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
        
        return new ScrollPanel(super.createSubmenu_panel(components, true), 185, 145);
    }

    //starting to read the NMEA senteces whiche were received from the serial port 
    protected void startGPS(String COMport) {
        //GPS layer
        try {
            SerialPortInfo myPortInfo = new SerialPortInfo("COM7", BaudRate.BAUD_9600, Parity.NONE, StopBits.ONE, 8);
            GeoPositionListener myGeo = new GeoPositionListener(map, super.gpsLayer);

            gpsWatcher = new SerialPortGPSWatcher(myPortInfo, myGeo);

            super.gpsLayer = new GPSLayer(gpsWatcher);
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

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // instance of this application
                    Aeronave_GUI aeronave = new Aeronave_GUI();

                    // create the UI, including the map, for the application.
                    JFrame appWindow = aeronave.createWindow();
                    appWindow.add(aeronave.createUI());
                    appWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    appWindow.setVisible(true);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });
    }

}
