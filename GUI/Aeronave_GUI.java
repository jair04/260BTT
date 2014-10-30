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
import com.esri.core.gps.BaudRate;
import com.esri.core.gps.FileGPSWatcher;
import com.esri.core.gps.GPSException;
import com.esri.core.gps.IGPSWatcher;
import com.esri.core.gps.Parity;
import com.esri.core.gps.SerialPortGPSWatcher;
import com.esri.core.gps.SerialPortInfo;
import com.esri.core.gps.StopBits;
import com.esri.map.GPSLayer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Jair
 */
public class Aeronave_GUI extends General_GUI {

    //private final JPanel gpsPanel = super.createButtonPanel("gpsOff", 100, 145);
    private final JScrollPane subGPS;
    private JPanel profiles;
    private final String offset = "                                                            ";
    private final Consumidor consumidor;
    GeoPositionListener myGeo;
    SerialPortGPSWatcher gpsWatcher;
    IGPSWatcher gpsWatcher1;

    public Aeronave_GUI() throws Exception {
        //Filling airchip general information 
        Aeronave aeronave = new Aeronave();
        aeronave.readFileInformation();

        super.setTypeUser(Constante.AERONAVE);
        super.getControlPanel().setTextTextField(aeronave.getMatricula());

        this.consumidor = new Consumidor("", aeronave, this);

        //GPS submenu
        subGPS = this.getGPS_PanelSubmenu();
        profiles = this.getProfile_panel();

        contentPane.add(subGPS);
        contentPane.add(profiles);
    }

    @Override
    public JComponent createUI() {
        ButtonPanel gpsPanel = null;
        ButtonPanel conectServer = null;
        ButtonPanel documents = null;

        try {
            gpsPanel = new ButtonPanel("gpsOff", 100, 145, subGPS);
            documents = new ButtonPanel("map", 10, 595, null);
            documents.setPanel(profiles);

            //Here we send the consumer, then inside we sent the ip and establish the comunication 
            conectServer = new ButtonPanel("conectServer", 10, 145, null);
            conectServer.setConsumidor(this.consumidor);
        } catch (IOException ex) {
            Logger.getLogger(Aeronave_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.contentPane.add(conectServer);
        super.contentPane.add(gpsPanel);
        super.contentPane.add(documents);
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

        final JCheckBox estado = new JCheckBox("Activar GPS" + offset);
        estado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!estado.isSelected()) {
                    String puerto = JOptionPane.showInputDialog(
                            null,
                            "Número de puerto COM: ",
                            JOptionPane.QUESTION_MESSAGE);
                    startGPS("COM" + puerto);
                } else {
                    try {
                        stopGPS();
                    } catch (IOException | GPSException ex) {
                        Logger.getLogger(Aeronave_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        final JCheckBox file = new JCheckBox("Leer NMEA" + offset);
        file.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!file.isSelected()) {
                    startGPS("");
                } else {
                    try {
                        stopGPS();
                    } catch (IOException | GPSException ex) {
                        Logger.getLogger(Aeronave_GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        components.add(navegacion);
        components.add(estado);
        components.add(file);

        return new ScrollPanel(super.createSubmenu_panel(components, true), 185, 145);
    }

    //starting to read the NMEA senteces which were received from the serial port 
    protected void startGPS(String COMport) {
        //GPS layer
        try {
            myGeo = new GeoPositionListener(this);

            if (!COMport.equals("")) {
                SerialPortInfo myPortInfo = new SerialPortInfo(COMport, BaudRate.BAUD_9600, Parity.NONE, StopBits.ONE, 8);
                gpsWatcher = new SerialPortGPSWatcher(myPortInfo, myGeo);
                super.gpsLayer = new GPSLayer(gpsWatcher);
            } else {
                String path = new java.io.File(".").getCanonicalPath() + "\\build\\classes\\Archivos\\GPS\\GPSReader.txt";
                gpsWatcher1 = new FileGPSWatcher(path, 500, true, myGeo);
                super.gpsLayer = new GPSLayer(gpsWatcher1);
            }

            super.gpsLayer.setMode(GPSLayer.Mode.NAVIGATION);
            super.gpsLayer.setNavigationPointHeightFactor(0.5);

            super.layersList.add(super.gpsLayer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "GPS no conectado");
        }
    }

    //stop reading gps NMEA sentences. 
    private void stopGPS() throws IOException, GPSException {
        myGeo.stop();
       
        if(gpsWatcher == null){
            gpsWatcher1.stop();;
        }else{
            gpsWatcher.stop();
        }
    }

    public JPanel getProfile_panel() {
        JPanel panel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth() - 120;
        int height = (int) screenSize.getHeight();

        panel.setSize(width, 171);
        panel.setLocation(105, height - 264);
        panel.setVisible(false);

        return panel;
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public JPanel getProfiles() {
        return profiles;
    }

    public void setProfiles(JPanel profiles) {
        this.profiles = profiles;
    }

}
